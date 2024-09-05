package com.shakilpatel.notesapp.ui.nav.navigations

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.shakilpatel.notesapp.ui.main.home.notes.ViewNotesScreen
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.nav.getViewModelInstance
import com.shakilpatel.notesapp.ui.nav.sharedViewModel

fun NavGraphBuilder.ViewNotesNav(navController: NavController) {
    navigation(
        startDestination = Screen.ViewNotes.Landing.route,
        route = Screen.ViewNotes.route + "/{notesId}"
    ) {
        composable(Screen.ViewNotes.Landing.route + "/{notesId}",
            arguments = listOf(
                navArgument("notesId") {
                    type = NavType.StringType
                }
            )
        ) {
            Log.d("TAG", "ViewNotesNav: " + it.destination.parent?.parent?.route)
            Log.d("TAG", "ViewNotesNav: " + navController.currentBackStackEntry?.destination?.parent?.route)
            ViewNotesScreen(
                it.arguments?.getString("notesId")!!,
                viewModel = navController.getViewModelInstance(navBackStackEntry = it, route = Screen.Main.Home.Landing.route),
                navController = navController
            )
        }
    }
}