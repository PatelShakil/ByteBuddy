package com.shakilpatel.notesapp.ui.main.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.TextColor
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.SnackBarCus
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.learning.CourseModel
import com.shakilpatel.notesapp.data.models.learning.SemModel
import com.shakilpatel.notesapp.data.notification.Cons
import com.shakilpatel.notesapp.data.notification.NotificationData
import com.shakilpatel.notesapp.data.notification.PushNotification
import com.shakilpatel.notesapp.ui.main.home.notes.NotesViewModel
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    notesViewModel: NotesViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
//    val notesList = viewModel.notesCol.collectAsState()
    ByteBuddyTheme {
        BackHandler {
            onBack()
        }
        val coursesList = viewModel.courses.collectAsState()
        coursesList.value.let {
            when (it) {
                is Resource.Success -> {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                HorizontalBrush
                            )
                    ) {
                        items(it.result) {
                            Sp(10.dp)
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

@Composable
fun ContactUsCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            MainColor
        )
    ) {
        Text(
            "Contact @Developer",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = TextColor
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
            val icon = if(isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
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
                Icon(
                    icon,
                    "",
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    }
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
                            if ((it.courseId + it.name).contains("BCA"))
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