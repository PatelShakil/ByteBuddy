package com.shakilpatel.bytebuddy.ui.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.tools.rememberGetContentContractLauncher
import com.shakilpatel.bytebuddy.common.uicomponents.CusDropdown
import com.shakilpatel.bytebuddy.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.bytebuddy.common.uicomponents.DatePickerDialogCustom
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.SignupTextField
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.learning.CollegeModel
import com.shakilpatel.bytebuddy.data.models.learning.EducationModel
import com.shakilpatel.bytebuddy.data.models.user.SavedModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme

@Composable
fun SignupScreen(viewModel: AuthViewModel, navController: NavController) {
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
        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var confirmPass by remember { mutableStateOf("") }
        var dob by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf("") }
        var profileImg by remember { mutableStateOf("") }
        var education by remember { mutableStateOf(EducationModel()) }
        var college by remember { mutableStateOf(CollegeModel()) }
        Box {
            Image(
                painterResource(R.drawable.login_bg),
                "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sp(h = 10.dp)
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
                            Color(0xFFEABCA0)
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
                            SignupTextField(
                                value = email, label = "Email*", onTextChanged = {
                                    email = it
                                },
                                keyboardType = KeyboardType.Email
                            )
                            SignupTextField(
                                label = "Password*", onTextChanged = {
                                    pass = it
                                },
                                keyboardType = KeyboardType.Password
                            )
                            SignupTextField(
                                label = "Confirm Password*", onTextChanged = {
                                    confirmPass = it
                                },
                                keyboardType = KeyboardType.Password
                            )
                            Sp(h = 20.dp)
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
                            Sp(h = 10.dp)

                            AnimatedVisibility(visible = name.isNotEmpty()) {
                                CustomOutlinedButton(
                                    label = "Create an account as $name",
                                    isEnable = (pass == confirmPass) && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() && dob.isNotEmpty() && gender.isNotEmpty() && education.courseName.isNotEmpty() && college.name.isNotEmpty()
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
                                            ), pass, navController
                                    )
                                }
                            }

                            Sp(h = 20.dp)
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
                                    .border(1.dp, MainColor, CircleShape),
                                colorFilter = ColorFilter.tint(MainColor),
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
                Sp(h = 20.dp)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account ? ")
                    Text("Login",
                        color = MainColor,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        })
                }
                val context = LocalContext.current
                viewModel.signupResult.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading -> {
                            ProgressBarCus {

                            }
                        }

                        is Resource.Failure -> {
                            LaunchedEffect(key1 = true) {
                                Toast.makeText(context, it.errorMsgBody, Toast.LENGTH_SHORT).show()
                            }
                        }

                        is Resource.Success -> {
                            LaunchedEffect(key1 = true) {
                                if (it.result) {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Auth.Signup.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }

            }
        }
    }
}