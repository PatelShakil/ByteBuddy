package com.shakilpatel.notesapp.ui.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.tools.pdflib.dp
import com.shakilpatel.notesapp.common.uicomponents.AddChatUserCard
import com.shakilpatel.notesapp.common.uicomponents.ConnectedChatUserCard
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.SearchBar
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.common.uicomponents.shimmerEffect
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import org.bouncycastle.asn1.x500.style.RFC4519Style.uid
import kotlin.random.Random

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
                SearchBar(hint = "Search your recent chats") {
                    searchText = it.lowercase()
                }
                Spacer(Modifier.height(5.dp))

                viewModel.connectedUsers.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading -> {
                            Column(
                                modifier =Modifier.fillMaxSize()
                            ){
                                val users = Random.nextInt(from = 2, until = 20)
                                repeat(users){
                                    val width = Random.nextInt(from = 120, until = 300)
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(horizontal = 5.dp, vertical = 3.dp)
                                            .clip(RoundedCornerShape(15.dp))
                                            .shimmerEffect()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Box(
                                            modifier= Modifier.size(50.dp)
                                                .clip(CircleShape)
                                                .shimmerEffect()
                                        )
                                        Box(
                                            modifier = Modifier.width(width.dp)
                                                .padding(start= 10.dp)
                                                .height(25.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .shimmerEffect()
                                        )
                                    }
                                }
                            }

                        }
                        is Resource.Failure -> {
                            OnNoDataFound(msg = it.errorMsgBody)
                        }
                        is Resource.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    val baseList = it.result
                                    val filterList = when(searchText){
                                        "" -> baseList
                                        else -> baseList.filter { it.first.name.lowercase().contains(searchText)}
                                    }
                                    if(baseList.isEmpty()){
                                        Column(
                                            modifier= Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ){
                                            Text("You're not having any chat yet", color = MainColor)
                                            Sp(h= 30.dp)

                                            Box(
                                                modifier= Modifier.size(60.dp)
                                                    .border(1.dp, MainColor, CircleShape)
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        navController.navigate(Screen.Main.Chat.AddChatUser.route)

                                                    },
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    Icons.Default.Add,
                                                    "",
                                                    tint = MainColor,
                                                    modifier = Modifier.scale(1.6f)
                                                )
                                            }

                                        }
                                    }
                                    if(filterList.isEmpty() && baseList.isNotEmpty()){
                                        OnNoDataFound(msg = "No user found")
                                    }
                                    filterList.sortedBy {  it.second.lastMsgTime}.reversed().forEachIndexed { index, pair ->
                                        ConnectedChatUserCard(
                                            user = pair.first,
                                            con = pair.second
                                        ) {
                                            viewModel.getRecieverUser(pair.first.uid)
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