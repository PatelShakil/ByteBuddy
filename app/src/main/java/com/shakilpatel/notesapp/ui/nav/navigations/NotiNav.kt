package com.shakilpatel.notesapp.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.notification.NotificationScreen

fun NavGraphBuilder.NotiNav(navController: NavController) {
    composable(Screen.Notification.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "bytebuddy://bytebuddy.com/notification"
            }
        )
    ){
        NotificationScreen(viewModel = hiltViewModel(), navController = navController)
    }
}