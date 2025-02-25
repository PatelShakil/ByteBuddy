package com.shakilpatel.bytebuddy.ui.main.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.learning.CourseModel
import com.shakilpatel.bytebuddy.data.models.learning.SemModel
import com.shakilpatel.bytebuddy.ui.ads.AdmobBanner
import com.shakilpatel.bytebuddy.ui.main.home.notes.NotesViewModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    notesViewModel: NotesViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
        ByteBuddyTheme {
            BackHandler {
                onBack()
            }
            val coursesList = viewModel.courses.collectAsState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(
                        HorizontalBrush
                    )
            ) {
                viewModel.banners.collectAsState().value.let {
                when (it) {
                    is Resource.Loading -> {
                        ProgressBarIndicator()
                    }

                    is Resource.Success -> {
                        if (it.result.isNotEmpty()) {
                            val uriHandler = LocalUriHandler.current
                            val state = rememberPagerState(initialPage = 0)
                            LaunchedEffect(Unit) {
                                while (true) {
                                    yield()
                                    delay(3000)
                                    state.animateScrollToPage(
                                        page = (state.currentPage + 1) % (state.pageCount)
                                    )
                                }
                            }

                            HorizontalPager(
                                count = it.result.size,
                                state = state
                            ) { page ->
                                val item =
                                    it.result.sortedBy { it.position }[page]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .padding(20.dp),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    onClick = {
                                        uriHandler.openUri(item.url)
                                    }
                                ) {
                                    AsyncImage(
                                        item.image,
                                        "",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            }
                        }
                    }

                    is Resource.Failure -> {}
                    else -> {}
                }
            }

                coursesList.value.let {
                when (it) {
                    is Resource.Success -> {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier= Modifier.fillMaxWidth()
                        ) {

                            item{
                                AdmobBanner(modifier= Modifier.fillMaxWidth())
                            }


                            items(it.result) {
                                CourseCard(title = it.name, it, notesViewModel, navController)
                                Sp(10.dp)
                            }
                            item {
                                Sp(15.dp)
                                ContactUsCard() {
                                    navController.navigate(Screen.Main.Profile.AboutUs.route)
                                }
                            }
                        }

                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Failure -> {
                        SnackBarCus(msg = it.errorMsgBody)
                    }

                    else -> {

                        ProgressBarIndicator()
                    }
                }
            }
            //    var bcaList by remember{ mutableStateOf(mutableListOf<NotesModel>()) }
            //    var plList by remember{ mutableStateOf(mutableListOf<NotesModel>()) }
            //    notesList.value.let {
            //        when(it){
            //            is Resource.Success ->{
            //                bcaList = it.result.filter { it.courseId.contains("BCA") }.toMutableList()
            //                plList = it.result.filter { it.courseId.contains("Programming Language") }.toMutableList()
            //            }
            //            else ->{}
            //        }
            //    }


        }
    }
}

@Composable
fun ContactUsCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
            .padding(top = 10.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            Color(0xFFFEFEFE)
        )
    ) {
        Image(
            painterResource(R.drawable.powered_by),
            "",
            modifier= Modifier.fillMaxSize()
                .padding(5.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourseCard(
    title: String,
    course: CourseModel,
    viewModel: NotesViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.95f)
            .padding(vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HorizontalBrush)
        ) {
            var isExpanded by rememberSaveable{mutableStateOf(false)}

            LaunchedEffect(key1 = true) {
                delay(250)
                isExpanded = true
            }

            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp),
                    fontFamily = FontFamily.Serif
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = MainColor
            )
            AnimatedVisibility(visible = isExpanded) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    course.semList.forEach {
                        SemCardItem(sem = it, onSemClick = {
                            viewModel.setCourse(it.courseId + it.name)
                            viewModel.setSubject(it.id)
                            if ((it.courseId + it.name).contains("BCA") || (it.courseId + it.name).contains("MSC ICT"))
                                navController.navigate(Screen.Main.Home.Subject.route + "/${it.courseId + it.name}")
                            else
                                navController.navigate(Screen.Main.Home.Notes.route + "/${it.id}/${it.courseId}")

                            Log.d("Sem HOme", viewModel.course.value)
                        })
                    }
                }
            }

        }
    }
}

@Composable
fun SemCardItem(sem: SemModel, thumbnail: String = "", onSemClick: (SemModel) -> Unit) {
//    val colorStart = getRandomLightColor()
//    val colorEnd = getRandomLightColor()
    val colorStart = MainColor
    val colorEnd = MainColor

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(colorStart, colorEnd)
                    )
                )
                .clickable {
                    onSemClick(sem)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = sem.id,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
//            color = calculateContrastingDarkTextColor(colorStart, colorEnd),
                color = WhiteColor,
                textAlign = TextAlign.Center
            )

        }

    }

}

@Composable
fun calculateContrastingDarkTextColor(colorStart: Color, colorEnd: Color): Color {
    val darkColor = Color(0xFF222222) // Choose a dark color (e.g., #222222)

    return if (colorStart.luminance() > colorEnd.luminance()) {
        darkColor
    } else {
        darkColor.copy(alpha = 0.8f) // Adjust alpha for a brighter dark color
    }
}