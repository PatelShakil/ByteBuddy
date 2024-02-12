package com.shakilpatel.notesapp.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.shakilpatel.notesapp.ui.main.chat.ChatLandingScreen
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.notification.NotificationScreen

fun NavGraphBuilder.ChatNav(navController: NavController,onBack : ()->Unit) {
    navigation(
        startDestination = Screen.Main.Chat.Landing.route,
        route = Screen.Main.Chat.route
    ) {
        composable(
            Screen.Main.Chat.Landing.route,
        ) {
            ChatLandingScreen(hiltViewModel(),navController){
                onBack()
            }
        }
    }
}