package com.shakilpatel.bytebuddy.ui.main.create

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.getHorizontalGradient
import com.shakilpatel.bytebuddy.common.tools.rememberGetContentContractLauncherMultiple
import com.shakilpatel.bytebuddy.common.uicomponents.CustomButtonBottomRightIcon
import com.shakilpatel.bytebuddy.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.bytebuddy.common.uicomponents.CustomTextField
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.TabHeader
import com.shakilpatel.bytebuddy.data.models.error.FAQModel

@Composable
fun FAQTabContent(viewModel: CreateViewModel, navController: NavController) {
    // Content of the "Errors" tab
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HorizontalBrush)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabHeader(tabName = "Upload FAQs")

            val context = LocalContext.current
            var eTitle by remember { mutableStateOf("") }
            var eDes by remember { mutableStateOf("") }
            var eCode by remember { mutableStateOf("") }
            var eSsList by remember { mutableStateOf(mutableListOf<String>()) }
            var isImagesSelected by remember { mutableStateOf(false) }
            val getContent = rememberGetContentContractLauncherMultiple(true) { uris ->
                if (uris.isNotEmpty()) {
                    Log.d("URIS", uris.toString())
                    val newImages = uris.map { uri ->
                        Cons.encodeImage(Cons.createBitmapFromUri(context, uri)!!)
                    }
                    eSsList.addAll(newImages)
                    isImagesSelected = true
                    Log.d("ESSLIST", eSsList.toString())
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(value = eTitle, label = "Question/Doubts*", onTextChanged = {
                    eTitle = it
                })
                Sp(5.dp)
                CustomTextField(
                    value = eDes,
                    label = "Details*",
                    hint = "Enter detail about your Errors/Questions",
                    onTextChanged = {
                        eDes = it
                    })
                CustomTextField(
                    value = eCode,
                    label = "Code",
                    hint = "if you have any Code than paste it here",
                    onTextChanged = {
                        eCode = it
                    })
                Sp(5.dp)
                val view = LocalView.current
                CustomButtonBottomRightIcon(modifier = Modifier, label = "Add Images/Screenshots") {
                    isImagesSelected = false
                    getContent.launch(
                        "image/*",
                        ActivityOptionsCompat.makeClipRevealAnimation(
                            view,
                            1,
                            1,
                            view.width,
                            view.height
                        )
                    )
                }
                Sp(5.dp)
                if (isImagesSelected) {
                    ESSCardListView(list = eSsList)
                }
                Sp(5.dp)
                AnimatedVisibility(visible = eTitle.isNotEmpty() && eDes.isNotEmpty()) {
                    CustomOutlinedButton(label = "Upload") {
                        val error = FAQModel(
                            Cons.generateRandomValue(9),
                            eTitle.trim(),
                            eDes.trim(),
                            FirebaseAuth.getInstance().uid.toString(),
                            eCode.trim(),
                            System.currentTimeMillis(),
                            emptyList(),
                            emptyList(),
                            eSsList
                        )
                        viewModel.uploadError(error)
                        eTitle = ""
                        eDes = ""
                        eCode = ""
                        eSsList = emptyList<String>().toMutableList()
                    }
                }
                Sp(h = 70.dp)

                viewModel.uploadErrorResult.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            Log.d("Error Result Success", it.result.toString())
                            SnackBarCus(msg = "Faq Published Successfully")
                        }

                        is Resource.Failure -> {
                            Log.d("Error Result Failure", it.errorMsgBody)
                            SnackBarCus(msg = it.errorMsgBody)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ESSCardListView(list: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(.95f),
        border = BorderStroke(1.dp, getHorizontalGradient()),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxHeight(.4f)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            list.forEach {
                Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                    SSCardItem(it)
                }
            }
        }
        Text(
            text = list.size.toString() + " Images selected",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun SSCardItem(image: String) {
    Card(modifier = Modifier.size(100.dp)) {
        if (image.isNotEmpty()) {
            Image(
                bitmap = Cons.decodeImage(image).asImageBitmap(), contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}
