package com.shakilpatel.bytebuddy.ui.main.profile

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.tools.rememberGetContentContractLauncher
import com.shakilpatel.bytebuddy.common.uicomponents.CusDropdown
import com.shakilpatel.bytebuddy.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.bytebuddy.common.uicomponents.CustomTextField
import com.shakilpatel.bytebuddy.common.uicomponents.DatePickerDialogCustom
import com.shakilpatel.bytebuddy.common.uicomponents.ProfileTextFieldDisabled
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.learning.CollegeModel
import com.shakilpatel.bytebuddy.data.models.learning.EducationModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.ui.main.home.ContactUsCard
import com.shakilpatel.bytebuddy.ui.nav.Screen

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController, onBack: () -> Unit) {
//    BackHandler {
//        onBack()
//    }
    var curUser by remember { mutableStateOf(UserModel()) }
    var eduList = listOf<EducationModel>()
    var collegeList = listOf<CollegeModel>()
    viewModel.getUploads()
    viewModel.getSaved()
    var isProfileEdit by remember { mutableStateOf(false) }
    viewModel.user.collectAsState().value.let {
        when (it) {
            is Resource.Success -> {
                curUser = it.result
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(WhiteColor)
                                    .padding(top = 5.dp, end = 5.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    isProfileEdit = !isProfileEdit
                                }) {
                                    Icon(Icons.Default.Edit, "", tint = MainColor)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(WhiteColor)
                                    .padding(top = 50.dp)
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
                                Sp(h = 10.dp)
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(Screen.Main.Profile.Uploaded.route)
                                        }
                                        .padding(10.dp)
                                        .padding(start = 20.dp),
                                        verticalAlignment = CenterVertically) {
                                        Text(
                                            "Your Uploads",
                                            modifier = Modifier.weight(.7f),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Sp(w = 10.dp)
                                    }
                                }
                                Sp(h = 10.dp)
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(Screen.Main.Profile.Saved.route)
                                        }
                                        .padding(10.dp)
                                        .padding(start = 20.dp),
                                        verticalAlignment = CenterVertically) {
                                        Text(
                                            "Your Saved",
                                            modifier = Modifier.weight(.7f),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Sp(w = 10.dp)
                                    }
                                }
                                Sp(h = 10.dp)
                                ContactUsCard {
                                    navController.navigate(Screen.Main.Profile.AboutUs.route)
                                }
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
                                    .align(TopCenter)
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
                                    .align(TopCenter)
                                    .background(WhiteColor, CircleShape)
                                    .border(1.dp, MainColor, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Sp(h = 50.dp)
                    if (isProfileEdit) {
                        EditProfileDialog(viewModel = viewModel) {
                            isProfileEdit = !isProfileEdit
                        }
                    }

                }
            }

            else -> {
                ProgressBarIndicator()
            }
        }
    }
}

@Composable
fun EditProfileDialog(viewModel: ProfileViewModel, onDismiss: () -> Unit) {
    var eduList = remember { mutableStateOf(listOf<EducationModel>()) }
    var collegeList = remember { mutableStateOf(listOf<CollegeModel>()) }
    var curUser by remember { mutableStateOf(UserModel()) }
    LaunchedEffect(key1 = true, block = {
        viewModel.getEducationList() {
            eduList.value = it
        }
        viewModel.getCollegeList() {
            collegeList.value = it
        }
    })
    viewModel.user.collectAsState().value.let {
        when (it) {
            is Resource.Success -> {
                curUser = it.result
                var profileImage by remember { mutableStateOf(curUser.profileImg ?: "") }
                Dialog(
                    onDismissRequest = onDismiss, properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(.95f)
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(top = 80.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(45.dp),
                            border = BorderStroke(1.dp, MainColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(HorizontalBrush)
                                    .padding(top = 80.dp)
                                    .padding(horizontal = 10.dp)
                                    .verticalScroll(rememberScrollState())
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomTextField(value = curUser.name,
                                    label = "Name",
                                    onTextChanged = {
                                        curUser.name = it
                                    })
                                Sp(h = 5.dp)
                                CusDropdown(select = curUser.gender,
                                    label = "Gender",
                                    options = listOf(
                                        "Male",
                                        "Female",
                                        "Others",
                                    ),
                                    onSelected = {
                                        curUser.gender = it
                                    })
                                Sp(h = 5.dp)
                                CusDropdown(select = curUser.education.courseName,
                                    label = "Education",
                                    options = eduList.value.map { it.courseName }.toList(),
                                    onSelected = {
                                        curUser.education = EducationModel(it)
                                    })
                                Sp(h = 5.dp)
                                CusDropdown(select = curUser.college.name,
                                    label = "College",
                                    options = collegeList.value.map { it.name }.toList(),
                                    onSelected = {
                                        curUser.college = CollegeModel(it)
                                    })
                                Sp(h = 5.dp)
                                DatePickerDialogCustom(date = curUser.dob,
                                    label = "DOB",
                                    onDateSelect = { y, m, d ->
                                        curUser.dob = "$d/$m/$y"
                                    })
                                Sp(h = 20.dp)
                                CustomOutlinedButton(label = "Update Profile") {
                                    curUser.profileImg = profileImage
                                    Log.d("PRofile IMage",profileImage)
                                    viewModel.updateProfile(curUser)
                                    onDismiss()
                                }
                                Sp(h = 50.dp)
                            }
                        }
                        val context = LocalContext.current
                        val getProfileImage = rememberGetContentContractLauncher(onResult = { uri ->
                            if (uri != null) {
                                profileImage =
                                    Cons.encodeImage(Cons.createBitmapFromUri(context, uri!!)!!)
                            }
                        })
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .align(TopCenter),
                            contentAlignment = Center
                        ) {
                            if (profileImage == null || profileImage == "") {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_profile_round_img),
                                    contentDescription = "",
                                    Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                        .background(WhiteColor, CircleShape)
                                )
                            } else {
                                Image(
                                    Cons.decodeImage(profileImage).asImageBitmap(),
                                    contentDescription = "",
                                    Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                        .background(WhiteColor, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Icon(painterResource(R.drawable.ic_camera),
                                "",
                                tint = MainColor,
                                modifier = Modifier
                                    .align(BottomEnd)
                                    .padding(bottom = 30.dp)
                                    .background(WhiteColor, CircleShape)
                                    .clip(CircleShape)
                                    .padding(5.dp)
                                    .clickable {
                                        getProfileImage.launch("image/*")

                                    })
                        }
                    }
                }
            }

            else -> {
                ProgressBarCus {

                }
            }
        }
    }
}
