package com.shakilpatel.notesapp.ui.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun ChatLandingScreen(viewModel: ChatViewModel,navController: NavController,onBack:()->Unit) {

    ByteBuddyTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Text("This is Chats Screen")
        }

    }

}