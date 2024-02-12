package com.shakilpatel.notesapp.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.shakilpatel.notesapp.ui.main.create.CreateScreen
import com.shakilpatel.notesapp.ui.main.feed.FeedScreen
import com.shakilpatel.notesapp.ui.main.home.HomeScreen
import com.shakilpatel.notesapp.ui.main.home.notes.NotesScreen
import com.shakilpatel.notesapp.ui.main.home.notes.NotesViewModel
import com.shakilpatel.notesapp.ui.main.home.notes.SubjectScreen
import com.shakilpatel.notesapp.ui.main.profile.AboutUsScreen
import com.shakilpatel.notesapp.ui.main.profile.ProfileScreen
import com.shakilpatel.notesapp.ui.main.profile.SavedContentScreen
import com.shakilpatel.notesapp.ui.main.profile.UploadContentScreen
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.nav.sharedViewModel

fun NavGraphBuilder.MainNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Main.Home.route,
        route = Screen.Main.route
    ) {
        HomeNav(navController) {
            onBack()
        }

//        FeedNav(navController){
//            onBack()
//        }
        composable(Screen.Main.Feed.Landing.route + "/{faqId}",
            arguments = listOf(
                navArgument("faqId"){
                    type= NavType.StringType
                }
            )
        ) {
            FeedScreen(it.arguments?.getString("faqId") ?: "", navController = navController) {
                onBack()
            }
        }
        CreateNav(navController) {
            onBack()
        }
        ProfileNav(navController) {
            onBack()
        }
        ChatNav(navController){
            onBack()
        }
    }
}

fun NavGraphBuilder.HomeNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Main.Home.Landing.route ,
        route = Screen.Main.Home.route
    ) {
        composable(Screen.Main.Home.Landing.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                hiltViewModel(),
                navController = navController
            ) {
                onBack()
            }
        }
        composable(
            Screen.Main.Home.Subject.route + "/{course}",
            arguments = listOf(navArgument("course") {
                type = NavType.StringType
            })
        ) {
            SubjectScreen(
                course = it.arguments?.getString("course")!!,
                viewModel = it.sharedViewModel<NotesViewModel>(navController = navController),
                navController = navController
            )
        }
        composable(
            Screen.Main.Home.Notes.route + "/{subjectName}/{courseName}",
            arguments = listOf(navArgument("subjectName") {
                type = NavType.StringType
            },
                navArgument("courseName") {
                    type = NavType.StringType
                }
            )
        ) {
            NotesScreen(
                it.arguments?.getString("subjectName")!!,
                it.arguments?.getString("courseName")!!,
                viewModel = it.sharedViewModel(navController = navController),
                navController = navController
            )
        }
        ViewNotesNav(navController)
    }
}

fun NavGraphBuilder.ProfileNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Main.Profile.Landing.route,
        route = Screen.Main.Profile.route
    ) {
        composable(Screen.Main.Profile.Landing.route) {
            ProfileScreen(viewModel = hiltViewModel(), navController = navController) {
                onBack()
            }
        }
        composable(Screen.Main.Profile.Saved.route) {
            SavedContentScreen(
                viewModel = it.sharedViewModel(navController = navController),
                navController = navController
            )
        }
        composable(Screen.Main.Profile.Uploaded.route) {
            UploadContentScreen(
                viewModel = it.sharedViewModel(navController = navController),
                navController = navController
            )
        }
        composable(Screen.Main.Profile.AboutUs.route) {
            AboutUsScreen(hiltViewModel())
        }
    }
}

fun NavGraphBuilder.FeedNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Main.Feed.Landing.route + "/${"#"}",
        route = Screen.Main.Feed.route
    ) {
        composable(Screen.Main.Feed.Landing.route + "/{faqId}",
            arguments = listOf(
                navArgument("faqId"){
                    type= NavType.StringType
                }
            )
        ) {
            FeedScreen(it.arguments?.getString("faqId")!!, navController = navController) {
                onBack()
            }
        }
    }
}

fun NavGraphBuilder.CreateNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Main.Create.Landing.route,
        route = Screen.Main.Create.route
    ) {
        composable(Screen.Main.Create.Landing.route) {
            CreateScreen(
                viewModel = it.sharedViewModel(navController = navController),
                navController = navController
            ) {
                onBack()
            }
        }
    }
}