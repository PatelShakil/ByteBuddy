package com.shakilpatel.notesapp.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.common.tools.rememberGetContentContractLauncher
import com.shakilpatel.notesapp.common.uicomponents.CusDropdown
import com.shakilpatel.notesapp.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.notesapp.common.uicomponents.DatePickerDialogCustom
import com.shakilpatel.notesapp.common.uicomponents.SignupTextField
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.learning.CollegeModel
import com.shakilpatel.notesapp.data.models.learning.EducationModel
import com.shakilpatel.notesapp.data.models.user.SavedModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun SignupScreen(email: String, viewModel: AuthViewModel, navController: NavController) {
    ByteBuddyTheme {
        var eduList = remember { mutableStateOf(listOf<EducationModel>()) }
        var collegeList = remember { mutableStateOf(listOf<CollegeModel>()) }
        LaunchedEffect(true) {
            viewModel.getEducationList() {
                eduList.value = it
            }
            viewModel.getCollegeList() {
                collegeList.value = it
            }
        }


        var name by remember { mutableStateOf("") }
        var dob by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf("") }
        var profileImg by remember { mutableStateOf("") }
        var education by remember { mutableStateOf(EducationModel()) }
        var college by remember { mutableStateOf(CollegeModel()) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HorizontalBrush)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Sp(h = 20.dp)
            Text(
                "Welcome to ByteBuddy",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp),
                fontFamily = FontFamily.Cursive

            )
            Text(
                "Creating account as $email",
                style = MaterialTheme.typography.titleSmall
            )
            Sp(h = 20.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth(.95f)
                    .fillMaxHeight()
            ) {
                Card(
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .fillMaxSize(),
                    colors = CardDefaults.cardColors(
                        Color.White
                    ),
                    shape = RoundedCornerShape(45.dp),
                    border = BorderStroke(1.dp, MainColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp)
                            .background(
                                WhiteColor
                            ), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Sp(h = 5.dp)
                        SignupTextField(
                            value = name, label = "Name*", onTextChanged = {
                                name = it
                            },
                            hint = "Enter your good name"
                        )
                        Sp(h = 5.dp)
                        CusDropdown(
                            select = gender,
                            label = "Gender*", options = listOf(
                                "Male",
                                "Female",
                                "Others",
                            ),
                            onSelected = {
                                gender = it
                            }
                        )
                        Sp(h = 5.dp)
                        CusDropdown(
                            select = education.courseName,
                            label = "Education*",
                            options = eduList.value.map { it.courseName }.toList(),
                            onSelected = {
                                education = EducationModel(it)
                            })
                        Sp(h = 5.dp)
                        CusDropdown(
                            select = college.name,
                            label = "College*",
                            options = collegeList.value.map { it.name },
                            onSelected = {
                                college = CollegeModel(it)
                            }
                        )
                        Sp(h = 5.dp)
                        DatePickerDialogCustom(
                            date = dob,
                            label = "DOB*",
                            onDateSelect = { y, m, d ->
                                dob = "$d/$m/$y"
                            }
                        )
                        Sp(20.dp)
                        AnimatedVisibility(visible = name.isNotEmpty()) {
                            CustomOutlinedButton(
                                label = "Create an account as $name",
                                isEnable = name.isNotEmpty() && dob.isNotEmpty() && gender.isNotEmpty() && education.courseName.isNotEmpty() && college.name.isNotEmpty()
                            ) {
                                viewModel.doSignup(
                                    UserModel(
                                        "",
                                        name,
                                        email,
                                        dob,
                                        gender,
                                        false,
                                        false,
                                        profileImg,
                                        education,
                                        college,
                                        System.currentTimeMillis(),
                                        "",
                                        0,
                                        SavedModel(),
                                        emptyList()
                                    ), navController
                                )
                            }
                        }

                        Sp(h = 30.dp)
                    }
                }
                val context = LocalContext.current
                val getProfileImage = rememberGetContentContractLauncher(onResult = { uri ->
                    if (uri != null) {
                        profileImg =
                            Cons.encodeImage(Cons.createBitmapFromUri(context, uri!!)!!)
                    }
                })
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.TopCenter),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImg == "") {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile_round_img),
                            contentDescription = "",
                            Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(WhiteColor, CircleShape)
                                .border(1.dp, MainColor, CircleShape)
                        )
                    } else {
                        Image(
                            Cons.decodeImage(profileImg).asImageBitmap(),
                            contentDescription = "",
                            Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(WhiteColor, CircleShape)
                                .border(1.dp, MainColor, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Icon(painterResource(R.drawable.ic_camera),
                        "",
                        tint = MainColor,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 30.dp)
                            .background(WhiteColor, CircleShape)
                            .clip(CircleShape)
                            .padding(5.dp)
                            .clickable {
                                getProfileImage.launch("image/*")

                            })
                }
            }
            Sp(h = 60.dp)
        }
    }
}