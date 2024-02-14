package com.shakilpatel.notesapp.ui.main.chat.addchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.uicomponents.AddChatUserCard
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.SearchBar
import com.shakilpatel.notesapp.ui.main.chat.ChatViewModel
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun AddChatUserScreen(viewModel : ChatViewModel,navController: NavController) {
    ByteBuddyTheme{
//        viewModel.getAllUsers()

        viewModel.allUsers.collectAsState().value.let {
            when(it){
                is Resource.Loading ->{
                    ProgressBarIndicator()
                }
                is Resource.Failure ->{
                    OnNoDataFound(msg = it.errorMsgBody)
                }
                is Resource.Success ->{
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var searchText by remember{ mutableStateOf("") }
                        SearchBar(hint = "Search Users") {
                            searchText = it
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