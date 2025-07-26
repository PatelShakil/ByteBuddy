package com.shakilpatel.bytebuddy.ui.main.chat.addchat

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.uicomponents.AddChatUserCard
import com.shakilpatel.bytebuddy.common.uicomponents.OnNoDataFound
import com.shakilpatel.bytebuddy.common.uicomponents.SearchBar
import com.shakilpatel.bytebuddy.common.uicomponents.shimmerEffect
import com.shakilpatel.bytebuddy.ui.main.chat.ChatViewModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlin.random.Random

@Composable
fun AddChatUserScreen(viewModel : ChatViewModel,navController: NavController) {
    val context = LocalContext.current
    ByteBuddyTheme{
//        viewModel.getAllUsers()

        viewModel.allUsers.collectAsState().value.let {
            when(it){
                is Resource.Loading ->{
                    Column(
                        modifier =Modifier.fillMaxSize()
                    ){
                        val users = Random.nextInt(from = 2, until = 20)
                        repeat(users){
                            val width = Random.nextInt(from = 120, until = 300)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp, vertical = 3.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .shimmerEffect()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Box(
                                    modifier= Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(width.dp)
                                        .padding(start = 10.dp)
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }                }
                is Resource.Failure ->{
                    OnNoDataFound(msg = it.errorMsgBody)
                }
                is Resource.Success ->{
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var searchText by remember{ mutableStateOf("") }
                        SearchBar(hint = "Search Users") {
                            searchText = it.lowercase()
                        }
                        val baseList = it.result
                        val usersList = when(searchText){
                            "" -> baseList
                            else -> baseList.filter {
                                it.name.contains(searchText,true)
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                if(usersList.isEmpty()){
                                    OnNoDataFound(msg = "No Data Found")
                                }
                            }
                            items(usersList) {
                                AddChatUserCard(user = it) {
                                    if(it.uid.isNullOrEmpty()){
                                        Toast.makeText(context, "User is removed...", Toast.LENGTH_SHORT).show()
                                        return@AddChatUserCard
                                    }
                                    viewModel.getRecieverUser(it.uid)
                                    navController.navigate(Screen.Main.Chat.ChatUser.route)

                                }
                            }

                        }
                    }
                }
                else->{
                }
            }
        }
    }
}