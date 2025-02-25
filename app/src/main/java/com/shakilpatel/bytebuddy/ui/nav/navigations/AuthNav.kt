package com.shakilpatel.bytebuddy.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shakilpatel.bytebuddy.ui.auth.AuthViewModel
import com.shakilpatel.bytebuddy.ui.auth.LoginScreen
import com.shakilpatel.bytebuddy.ui.auth.SignupScreen
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.nav.sharedViewModel

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
        composable(Screen.Auth.Signup.route ,
        ) {
            SignupScreen(
                viewModel = it.sharedViewModel<AuthViewModel>(navController = navController),
                navController
            )
        }
    }
}