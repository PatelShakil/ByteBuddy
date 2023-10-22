package com.shakilpatel.notesapp.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shakilpatel.notesapp.ui.auth.AuthViewModel
import com.shakilpatel.notesapp.ui.auth.LoginScreen
import com.shakilpatel.notesapp.ui.auth.SignupScreen
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.nav.sharedViewModel

fun NavGraphBuilder.AuthNav(navController: NavController, onBack: () -> Unit) {
    navigation(
        startDestination = Screen.Auth.Login.route,
        route = Screen.Auth.route
    ) {
        composable(Screen.Auth.Login.route) {
            LoginScreen(viewModel = hiltViewModel(), navController) {
                onBack()
            }
        }
        composable(Screen.Auth.Signup.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) {
            SignupScreen(
                email = it.arguments?.getString("email")!!,
                viewModel = it.sharedViewModel<AuthViewModel>(navController = navController),
                navController
            )
        }
    }
}