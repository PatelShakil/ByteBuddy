package com.shakilpatel.notesapp.ui.main.usersprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.common.uicomponents.ProfileTextFieldDisabled
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.ui.main.home.ContactUsCard
import com.shakilpatel.notesapp.ui.main.profile.EditProfileDialog
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun UsersProfileScreen(uid : String,viewModel: UsersProfileViewModel,navController: NavController) {
    ByteBuddyTheme {
        LaunchedEffect(key1 = true) {
            viewModel.getUser(uid)

        }

        viewModel.user.collectAsState().value.let {
            when(it){
                is Resource.Loading-> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()

                    }
                }
                is Resource.Failure->{}
                is Resource.Success->{
                    val curUser = it.result
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(HorizontalBrush)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.95f)
                                .fillMaxHeight()
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(top = 70.dp)
                                    .fillMaxSize(),
                                shape = RoundedCornerShape(45.dp),
                                border = BorderStroke(1.dp, MainColor),
                                colors = CardDefaults.cardColors(
                                    Color.Transparent
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(WhiteColor)
                                        .padding(top = 80.dp)
                                        .padding(horizontal = 10.dp)
                                ) {
                                    ProfileTextFieldDisabled(label = "Name", value = curUser.name)
                                    Sp(h = 5.dp)
                                    ProfileTextFieldDisabled(label = "Gender", value = curUser.gender)
                                    Sp(h = 5.dp)
                                    ProfileTextFieldDisabled(
                                        label = "Education", value = curUser.education.courseName
                                    )
                                    Sp(h = 5.dp)
                                    ProfileTextFieldDisabled(
                                        label = "College", value = curUser.college.name
                                    )
                                    Sp(h = 5.dp)
                                    ProfileTextFieldDisabled(label = "DOB", value = curUser.dob)
                                    Sp(h = 30.dp)
                                }
                            }
                            if (curUser.profileImg == null || curUser.profileImg == "") {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_profile_round_img),
                                    contentDescription = "",
                                    Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.TopCenter)
                                        .background(WhiteColor, CircleShape)
                                        .border(1.dp, MainColor, CircleShape)
                                )
                            } else {
                                Image(
                                    Cons.decodeImage(curUser.profileImg!!).asImageBitmap(),
                                    contentDescription = "",
                                    Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.TopCenter)
                                        .background(WhiteColor, CircleShape)
                                        .border(1.dp, MainColor, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Sp(h = 50.dp)

                    }
                }
                else->{}
            }
        }

    }
}