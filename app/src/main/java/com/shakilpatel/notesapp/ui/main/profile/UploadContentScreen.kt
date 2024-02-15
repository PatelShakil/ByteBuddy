package com.shakilpatel.notesapp.ui.main.profile

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.shakilpatel.notesapp.common.uicomponents.FAQIterationStr
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.main.feed.error.FAQViewModel
import com.shakilpatel.notesapp.ui.main.home.notes.NotesItem
import com.shakilpatel.notesapp.ui.main.home.notes.NotesViewModel
import com.shakilpatel.notesapp.ui.main.home.notes.Tabs
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun UploadContentScreen(viewModel: ProfileViewModel, navController: NavController) {
    ByteBuddyTheme {
        UploadTabLayout(viewModel = viewModel, navController = navController)
    }

}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun UploadTabLayout(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        2
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column {
            Tabs(
                pagerState = pagerState,
                modifier = Modifier.weight(.05f),
                listOf("Faq", "Notes")
            )
            Box(
                modifier = Modifier
                    .weight(.8f)
                    .fillMaxSize()
            ) {
                UploadTabsContent(pagerState = pagerState, navController)
            }

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun UploadTabsContent(pagerState: PagerState, navController: NavController) {
    val context = LocalContext.current
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                UpFaqSampleScreen(viewModel = hiltViewModel(), hiltViewModel(),navController)
            }

            1 -> {
                UpNotesSampleScreen(
                    viewModel = hiltViewModel(), hiltViewModel(), navController
                )
            }

        }
    }

}

@Composable
fun UpNotesSampleScreen(
    viewModel: NotesViewModel, profViewModel: ProfileViewModel, navController: NavController
) {
    val context = LocalContext.current
    val user = remember { mutableStateOf(UserModel()) }
    viewModel.getUserModel {
        user.value = it
    }
    val notesList = remember { mutableStateOf(mutableListOf<NotesModel>()) }
    profViewModel.getUploads(user.value.uid) {
        notesList.value = it.notes
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (notesList.value.isEmpty()) {
            OnNoDataFound(msg = "No Such notes you uploaded")
        }
        LazyVerticalGrid(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceAround,
            content = {
                items(notesList.value) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        NotesItem(notes = it, viewModel, navController)
                    }

                }
            })
    }

}

@Composable
fun UpFaqSampleScreen(viewModel: FAQViewModel, profViewModel: ProfileViewModel,navController: NavController) {
    Sp(h = 5.dp)
    var user by remember { mutableStateOf(UserModel()) }
    viewModel.getUserModel {
        user = it
    }
    val errorList = remember { mutableStateOf(mutableListOf<FAQModel>()) }
    viewModel.getUploads(user.uid) {
        errorList.value = it.toMutableList()
    }
    ByteBuddyTheme {
        if (errorList.value.isNotEmpty()) {
            Log.d("User", user.toString())
            FAQIterationStr(faqList = errorList.value.map { it.id }, viewModel = viewModel, navController)
        } else
            ProgressBarIndicator()
    }
//    LazyColumn(
//        horizontalAlignment = CenterHorizontally, modifier = Modifier.fillMaxSize()
//    ) {
//        if (errorList.value.isEmpty()) {
//            item {
//                OnNoDataFound(msg = "No such errors found")
//            }
//        }
//        items(errorList.value) {
//            if (it != FAQModel()) {
//                FAQItem(
//                    faq = it,
//                    viewModel = viewModel
//                )
//                Sp(h = 10.dp)
//            }
//
//        }
//    }
}

