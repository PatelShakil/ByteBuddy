package com.shakilpatel.notesapp.ui.main.home.notes

import android.app.Activity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.ads.AdmobBanner
import com.shakilpatel.notesapp.ui.ads.showInterstialAd
import com.shakilpatel.notesapp.ui.nav.Screen


@Composable
fun NotesScreen(
    subjectName: String,
    courseName: String,
    viewModel: NotesViewModel,
    navController: NavController
) {

    LaunchedEffect(key1 = true, block = {
        viewModel.getNotesCol(subjectName, courseName)

    })

    Column() {
        NotesAppBar(title = subjectName, navController) {
//            Icon(Icons.Default.Search, contentDescription = "" )
        }
        viewModel.notesCol.collectAsState().value.let {
            when (it) {
                is Resource.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(HorizontalBrush),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
//                        val dummyList =
//                            it.result + it.result + it.result + it.result + it.result + it.result + it.result

                        item {
                            Sp(h = 20.dp)
                            if (it.result.isEmpty())
                                OnNoDataFound(msg = "Try again after some time.")
                        }
                        item {
                            NotesColCard(
                                title = "All", list = it.result.sortedBy { it.date }.reversed(),
//                                title = "All", list = dummyList,
                                viewModel, navController
                            )
                            Sp(10.dp)
                        }
                        item{
                            AdmobBanner(modifier= Modifier.padding(vertical = 10.dp))
                        }
                        item {
                            if (it.result.filter { it.verified }.isNotEmpty()) {
                                NotesColCard(
                                    title = "Official",
                                    list = it.result.filter { it.verified }.sortedBy { it.date }
                                        .reversed(),
                                    viewModel, navController
                                )
                                Sp(10.dp)
                            }
                        }
                        item{
                            AdmobBanner(modifier= Modifier.padding(vertical = 10.dp))
                        }
                        item {
                            if (it.result.filter { !it.verified }.isNotEmpty()) {
                                NotesColCard(
                                    title = "Unofficial",
                                    list = it.result.filter { !it.verified }.sortedBy { it.date }
                                        .reversed(),
                                    viewModel, navController
                                )
                                Sp(10.dp)
                            }
                        }
                        item {
                            Sp(h = 70.dp)
                        }

                    }
                }

                is Resource.Failure -> {
                    OnNoDataFound(msg = it.errorMsgBody)
                }

                else -> {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProgressBarIndicator()
                        Sp(10.dp)
                        OnNoDataFound(msg = "Try again after some time.")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotesColCard(
    title: String,
    list: List<NotesModel>,
    viewModel: NotesViewModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(.95f),
        border = BorderStroke(1.dp, getHorizontalGradient()),
        colors = CardDefaults.cardColors(
            Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .background(MainColor),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(title, textAlign = TextAlign.Center, color = TextColor)
                }
            }
            var notesList by remember { mutableStateOf(listOf<NotesModel>()) }
            var notesListOg by remember { mutableStateOf(listOf<NotesModel>()) }

            if (expanded) {
                notesListOg = list
                var query by remember { mutableStateOf("") }
                SearchBar(
                    hint = "Search notes in ${if(list[0].subjectId.length >= 8 ) list[0].subjectId.substring(0, 8) + "..." else list[0].subjectId}",
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
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    if (notesList.isNotEmpty()) {
                        notesList.forEach() {
                            Box(contentAlignment = Center, modifier = Modifier) {
                                NotesItem(notes = it, viewModel, navController)
                            }
                        }
                    } else {
                        OnNoDataFound(msg = "No such notes found")
                    }
                }
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