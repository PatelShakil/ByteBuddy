package com.shakilpatel.notesapp.ui.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.tools.pdflib.dp
import com.shakilpatel.notesapp.common.uicomponents.AddChatUserCard
import com.shakilpatel.notesapp.common.uicomponents.ConnectedChatUserCard
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.SearchBar
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import org.bouncycastle.asn1.x500.style.RFC4519Style.uid

@Composable
fun ChatLandingScreen(viewModel: ChatViewModel,navController: NavController,onBack:()->Unit) {

    ByteBuddyTheme {
        LaunchedEffect(key1 = true) {
            viewModel.getConnectedUsers()
        }
        Box(
            modifier =Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                var searchText by remember { mutableStateOf("") }
                SearchBar(hint = "Search your friends...") {
                    searchText = it
                }
                Spacer(Modifier.height(5.dp))

                viewModel.connectedUsers.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }
                        is Resource.Failure -> {
                            OnNoDataFound(msg = it.errorMsgBody)
                        }
                        is Resource.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    it.result.second.sortedBy {  it.lastMsgTime}.reversed().forEachIndexed { index, con ->
                                        ConnectedChatUserCard(
                                            user = it.result.first.find { it.uid == con.receiverId }!!,
                                            con = con
                                        ) {
                                            viewModel.getRecieverUser(it.result.first.find { it.uid == con.receiverId }!!.uid)
                                            navController.navigate(Screen.Main.Chat.ChatUser.route)
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            FloatingActionButton(onClick = {
                                           navController.navigate(Screen.Main.Chat.AddChatUser.route)

            },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)) {
                Icon(
                    Icons.Default.Add,
                    "",
                    tint = MainColor
                )
            }
        }

    }

}