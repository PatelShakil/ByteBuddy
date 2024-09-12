package com.shakilpatel.notesapp.ui.main.home.notes

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.TextColor
import com.shakilpatel.notesapp.common.getHorizontalGradient
import com.shakilpatel.notesapp.common.getVerticalGradient
import com.shakilpatel.notesapp.common.uicomponents.NotesAppBar
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.SearchBar
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.common.uicomponents.header
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.ads.AdmobBanner
import com.shakilpatel.notesapp.ui.ads.showInterstialAd
import com.shakilpatel.notesapp.ui.nav.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NotesScreen(
    subjectName: String,
    courseName: String,
    viewModel: NotesViewModel,
    navController: NavController
) {

    Column {
        NotesAppBar(title = subjectName, navController) {
//            Icon(Icons.Default.Search, contentDescription = "" )
        }

        val state = rememberPagerState()
        val tabList = listOf("All","Official","Unofficial")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MainColor)
                .padding( horizontal = 10.dp)
        ) {
            val scope = rememberCoroutineScope()
            tabList.forEachIndexed { index, s ->
                Card(
                    modifier= Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    colors = CardDefaults.cardColors(
                        if (state.currentPage == index) Color.White else MainColor
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = BorderStroke(1.dp,if(state.currentPage == index) MainColor else Color.White),
                    onClick = {
                        scope.launch {
                            state.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(s,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        textAlign = TextAlign.Center,
                        color = if(state.currentPage == index) MainColor else Color.White)
                }
            }
        }

        val context = LocalContext.current

        viewModel.notesCol.collectAsState().value.let {
            when (it) {
                is Resource.Success -> {
                    HorizontalPager(count = tabList.size, state = state) {page->
                        when(page){
                            0 -> NotesList(it.result, viewModel, navController)
                            1 -> NotesList(it.result.filter { it.verified }, viewModel, navController)
                            2 -> NotesList(it.result.filter { !it.verified }, viewModel, navController)
                        }
                    }
                }

                is Resource.Failure -> {
                    val imageLoader = ImageLoader.Builder(context)
                        .components {
                            if (SDK_INT >= 28) {
                                add(ImageDecoderDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }
                        }
                        .build()

                    Image(
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(context).data(data = R.drawable.gif_under_development).apply(block = {
                                size(Size.ORIGINAL)
                            }).build(), imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    OnNoDataFound(msg = it.errorMsgBody)
                }
                is Resource.Loading ->{
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProgressBarIndicator()
                    }
                }

                else -> {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OnNoDataFound(msg = "Try again after some time.")
                    }
                }
            }
        }
    }
}

@Composable
fun NotesList(list : List<NotesModel>,viewModel: NotesViewModel,navController: NavController) {
    var notesList by remember { mutableStateOf(listOf<NotesModel>()) }
    var notesListOg by remember { mutableStateOf(listOf<NotesModel>()) }

    Column {
        notesListOg = list
        var query by remember { mutableStateOf("") }
        SearchBar(
            hint = "Search notes",
            "",
            onTextChanged = {
                query = it
            })
        if (query != "") {
            notesList = if (query != "")
                notesListOg.filter {
                    it.title.lowercase().contains(query) || it.subjectId.lowercase()
                        .contains(query) || it.courseId.lowercase().contains(query)
                }
            else
                notesListOg
        } else {
            notesList = notesListOg
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(HorizontalBrush),
        ) {

            notesList.forEachIndexed { index, notesModel ->
                if((index) % 6 == 0 && index != 0){
                    header {
                        AdmobBanner()
                    }
                }
                item {
                    Box(contentAlignment = Center, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        NotesItem(notes = notesModel, viewModel, navController)
                    }
                }
            }

            if(notesList.isEmpty()){
                header{
                    OnNoDataFound(msg = "No such notes found")
                }
            }
            header {
                AdmobBanner()
            }


        }
    }
}


@Composable
fun NotesItem(notes: NotesModel, viewModel: NotesViewModel, navController: NavController) {
    val context = LocalContext.current
    var user by remember { mutableStateOf(UserModel()) }
    var isSaved by remember { mutableStateOf(false) }
    viewModel.getUserModel(notes.author) {
        user = it
    }
    viewModel.getUserModel {
        isSaved = it.saved.notes.contains(notes.id)
    }
    Box(
        modifier = Modifier
            .size(150.dp)
    ) {
        Card(
            modifier = Modifier
                .size(140.dp)
                .clickable {
                    viewModel.notesId.value = notes.id
                    viewModel.registerView(notes.id)
                    showInterstialAd(context as Activity) {
                        viewModel.getNote(notes.id)
                        navController.navigate(Screen.ViewNotes.Landing.route + "/${notes.id}")
                    }
                },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(.5.dp, getHorizontalGradient())
        ) {
            Column(
                modifier = Modifier
                    .size(140.dp)
                    .background(getVerticalGradient()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = notes.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = TextColor
                )
            }

        }
        Icon(painterResource(if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
            "",
            modifier = Modifier
                .clickable {
                    viewModel.saveNotes(notes.id)
                }
                .align(BottomEnd)
                .size(30.dp),
            tint = MainColor
        )
    }
}