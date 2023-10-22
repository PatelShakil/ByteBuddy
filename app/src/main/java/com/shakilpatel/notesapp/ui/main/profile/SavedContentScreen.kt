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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.uicomponents.FAQIterationStr
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.main.feed.error.FAQViewModel
import com.shakilpatel.notesapp.ui.main.home.notes.NotesItem
import com.shakilpatel.notesapp.ui.main.home.notes.NotesViewModel
import com.shakilpatel.notesapp.ui.main.home.notes.Tabs
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun SavedContentScreen(viewModel: ProfileViewModel, navController: NavController) {

    ByteBuddyTheme {
        LaunchedEffect(true) {
            viewModel.getUserModel()
        }
        viewModel.user.collectAsState().value.let {
            when (it) {
                is Resource.Success -> {
                    SavedTabLayout(
                        viewModel = viewModel,
                        navController
                    )
                }

                else -> {
                    OnNoDataFound(msg = "You doesn't saved anything")
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun SavedTabLayout(
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
                ProfileTabsContent(pagerState = pagerState, navController)
            }

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun ProfileTabsContent(pagerState: PagerState, navController: NavController) {
    val context = LocalContext.current
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                FaqSampleScreen(viewModel = hiltViewModel())
            }

            1 -> {
                NotesSampleScreen(
                    viewModel = hiltViewModel(), navController
                )
            }

        }
    }

}

@Composable
fun NotesSampleScreen(
    viewModel: NotesViewModel, navController: NavController
) {
    val user = remember { mutableStateOf(UserModel()) }
    viewModel.getUserModel {
        user.value = it
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (user.value.saved.notes.isEmpty()) {
            OnNoDataFound(msg = "No Such notes saved")
        }
        LazyVerticalGrid(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceAround,
            content = {
                items(user.value.saved.notes) {
                    val note = remember { mutableStateOf(NotesModel()) }
                    viewModel.getNote(it) {
                        when (it) {
                            is Resource.Success -> {
                                note.value = it.result

                            }

                            else -> {}
                        }
                    }
                    if (note.value != NotesModel()) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            NotesItem(notes = note.value, viewModel, navController)
                        }
                    }

                }
            })
    }

}

@Composable
fun FaqSampleScreen(viewModel: FAQViewModel) {
    Sp(h = 5.dp)
    var user by remember { mutableStateOf(UserModel()) }
    viewModel.getUserModel {
        user = it
    }
//    viewModel.getSaved()
    ByteBuddyTheme {
//        viewModel.saved.collectAsState().value.let {
//            when (it) {
//                is Resource.Success -> {
//                    FAQIteration(faqList = it.result.errors, viewModel = viewModel)
//                }
//
//                else -> {}
//            }
//        }
        if (user != UserModel()) {
            Log.d("User", user.toString())
            FAQIterationStr(faqList = user.saved.errors, viewModel = viewModel)
        } else
            ProgressBarIndicator()
    }
}

