package com.shakilpatel.bytebuddy.ui.main.create

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.tools.rememberGetContentContractLauncher
import com.shakilpatel.bytebuddy.common.uicomponents.CusDropdown
import com.shakilpatel.bytebuddy.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.bytebuddy.common.uicomponents.CustomTextField
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.TabHeader
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.learning.SemModel

@Composable
fun NotesTabContent(viewModel: CreateViewModel, navController: NavController) {
    // Content of the "Notes" tab
    var userId by remember { mutableStateOf("") }
    viewModel.getCoursesList()
    userId = FirebaseAuth.getInstance().uid.toString()
    val context = LocalContext.current
    val notesId = Cons.generateRandomValue(12)
    var pdfUri by remember { mutableStateOf("") }
    var pdfTitle by remember { mutableStateOf("") }
    var pdfDes by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var courseUp by remember { mutableStateOf("") }
    var sem by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    val getContent = rememberGetContentContractLauncher(onResult = {
        pdfUri = it.toString()
    })
    val courses = viewModel.courses.collectAsState()
    var courseName by remember { mutableStateOf(mutableListOf<String>()) }
    var semName by remember { mutableStateOf(mutableListOf<String>()) }
    var semList by remember { mutableStateOf(mutableListOf<SemModel>()) }
    var plList by remember { mutableStateOf(mutableListOf<String>()) }
    courses.value.let {
        when (it) {
            is Resource.Success -> {
                courseName = it.result.map { it.name }.toMutableList()
                semName =
                    it.result.find { it.name == course }?.semList?.map { it.name }?.toMutableList()
                        ?: mutableListOf()
                plList =
                    it.result.filter { it.name == "Programming Language" }[0].semList.map { it.name }
                        .toMutableList()
                semList = it.result.find { it.name == course }?.semList?.toMutableList()
                    ?: mutableListOf()
            }

            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HorizontalBrush)
    ) {
        TabHeader(tabName = "Upload Notes")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.95f),
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(250.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(18.dp)
                                .fillMaxWidth()
                                .background(Color.Transparent, RoundedCornerShape(20.dp)),
//                            elevation = CardDefaults.cardElevation(5.dp),
//                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, MainColor),
                            colors = CardDefaults.cardColors(
                                Color.Transparent,
                                contentColor = Color.Transparent
                            )
                        ) {
//                            Sp(h = 50.dp, w = 50.dp)
                            if (pdfUri != "") {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pdf),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .background(Color.Transparent),
                                    tint = MainColor,

                                    )
                            } else {
                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_image),
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .background(Color.Transparent),
                                    tint = MainColor,

                                    )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)

                                .clip(CircleShape)
                                .size(40.dp)
                                .background(MainColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (pdfUri == "") {
                                Icon(
                                    painterResource(id = R.drawable.ic_add),
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            getContent.launch("application/pdf")
                                        }
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            pdfUri = ""
                                        }
                                )
                            }

                        }
                    }
                }
                Text(
                    "**your notes file should be goes in unverified section**",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 90.dp, start = 20.dp, end = 20.dp)
                )

            }
            Column(
                modifier = Modifier.fillMaxWidth(.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sp(5.dp)
                CustomTextField(value = pdfTitle, label = "Title*", onTextChanged = {
                    pdfTitle = it
                })
                Sp(5.dp)
                CustomTextField(value = pdfDes, label = "Description*", onTextChanged = {
                    pdfDes = it
                })
                CusDropdown(
                    "Course*",
                    options = courseName,
                    onSelected = {
                        course = it
                        courseUp = it
                        subject = ""
                    }
                )

                if (course == "Programming Language") {
                    CusDropdown(label = "Programming Language*", options = plList, onSelected = {
                        subject = it
                    })
                } else if (course.isNotEmpty()) {
                    CusDropdown(label = "Semester*", options = semName, onSelected = {
                        sem = it
                        courseUp = course + it
                    })
                    if (sem != "") {
                        CusDropdown(
                            label = "Subject*",
                            options = semList.filter { it.name == sem }[0].subjects.map { it.subjectName },
                            onSelected = {
                                subject = it
                            })
                    }
                }
                Sp(20.dp)
                AnimatedVisibility(
                    visible = course.isNotEmpty() &&
                            subject.isNotEmpty() &&
                            pdfTitle.isNotEmpty() &&
                            pdfUri.isNotEmpty()
                ) {
                    CustomOutlinedButton(
                        label = "Upload", isEnable = course.isNotEmpty() &&
                                subject.isNotEmpty() &&
                                pdfTitle.isNotEmpty() &&
                                pdfUri.isNotEmpty()
                    ) {
                        val notes = NotesModel(
                            notesId,
                            subject.trim(),
                            courseUp.trim(),
                            pdfTitle.trim(),
                            pdfDes.trim(),
                            pdfUri.trim(),
                            userId,
                            false,
                            emptyList(),
                            emptyList(),
                            System.currentTimeMillis()
                        )
                        Log.d("Notes", notes.toString())
                        subject = ""
                        courseUp = ""
                        pdfTitle = ""
                        pdfDes = ""
                        pdfUri = ""
                        viewModel.uploadNotes(context, notes)
                    }
                }

                viewModel.uploadResult.collectAsState().value.let {
                    when (it) {
                        is Resource.Success -> {
                            SnackBarCus(msg = "Uploaded Successfully")
                            LaunchedEffect(true) {
                                Toast.makeText(
                                    context,
                                    "Notes Uploaded Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is Resource.Failure -> {
                            SnackBarCus(msg = it.errorMsgBody)
                            LaunchedEffect(true) {
                                Toast.makeText(context, it.errorMsgBody, Toast.LENGTH_SHORT).show()

                            }
                        }

                        else -> {

                        }
                    }
                }

            }
            Sp(h = 70.dp)
        }

    }
}