package com.shakilpatel.bytebuddy.ui.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.bytebuddy.common.getViewModelInstance
import com.shakilpatel.bytebuddy.ui.main.chat.ChatLandingScreen
import com.shakilpatel.bytebuddy.ui.main.chat.addchat.AddChatUserScreen
import com.shakilpatel.bytebuddy.ui.main.chat.details.ChatDetailScreen
import com.shakilpatel.bytebuddy.ui.nav.Screen

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
        composable(
            Screen.Main.Chat.AddChatUser.route
        ){
            AddChatUserScreen(viewModel = navController.getViewModelInstance(
                navBackStackEntry = it,
                route = Screen.Main.Chat.Landing.route
            ), navController = navController)
        }

        composable(
            Screen.Main.Chat.ChatUser.route
        ){
            ChatDetailScreen(viewModel = navController.getViewModelInstance(
                navBackStackEntry = it,
                route = Screen.Main.Chat.Landing.route
            ), navController = navController)
        }
    }
}