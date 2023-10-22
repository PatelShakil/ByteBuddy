package com.shakilpatel.notesapp.ui.nav.navigations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.shakilpatel.notesapp.ui.main.home.notes.ViewNotesScreen
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.nav.sharedViewModel

fun NavGraphBuilder.ViewNotesNav(navController: NavController){
    navigation(startDestination = Screen.ViewNotes.Landing.route,
        route = Screen.ViewNotes.route + "/{notesId}"){
        composable(Screen.ViewNotes.Landing.route + "/{notesId}",
            arguments = listOf(
                navArgument("notesId"){
                    type = NavType.StringType
                }
            )
        ){
            ViewNotesScreen(it.arguments?.getString("notesId")!!,viewModel = it.sharedViewModel(navController = navController), navController = navController)
        }
    }
}