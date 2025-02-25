package com.shakilpatel.bytebuddy.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.notification.NotificationScreen

fun NavGraphBuilder.NotiNav(navController: NavController) {
    composable(Screen.Notification.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "bytebuddy://bytebuddy.com/notification"
            }
        )
    ){
        NotificationScreen(viewModel = hiltViewModel(), hiltViewModel(), navController = navController)
    }
}