@file:OptIn(ExperimentalMaterial3Api::class)

package com.shakilpatel.bytebuddy.ui.main.feed.error

//import com.google.accompanist.pager.PagerDefaults
//import com.google.accompanist.pager.VerticalPager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.getVerticalGradient
import com.shakilpatel.bytebuddy.common.uicomponents.FAQAppBar
import com.shakilpatel.bytebuddy.common.uicomponents.FAQIteration
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.ZoomableImage
import com.shakilpatel.bytebuddy.data.models.error.FAQAnsModel
import com.shakilpatel.bytebuddy.data.models.error.FAQModel
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme


@Composable
fun FaqScreen(faqId:String,viewModel: FAQViewModel, navController: NavController) {
    val list = viewModel.errorsList.collectAsState()
    ByteBuddyTheme {
        list.value.let {
            when (it) {
                is Resource.Success -> {
                    var dummylist by remember { mutableStateOf(listOf<FAQModel>()) }
                    var listOg by remember { mutableStateOf(listOf<FAQModel>()) }
                    listOg = it.result
//                        it.result + it.result + it.result + it.result + it.result + it.result + it.result + it.result + it.result
                    var resultCount by remember { mutableStateOf(0) }
                    var query by remember { mutableStateOf(faqId.replace("#","")) }
                    FAQAppBar(title = "FAQ",faqId.replace("#",""), resultCount, navController = navController) { value ->
                        query = value
//                        dummylist = if (value.isNotEmpty()) {
//                            listOg.filter {
//                                it.title.lowercase().contains(value) || it.description.lowercase()
//                                    .contains(value)
//                            }
//                        } else
//                            listOg
//                        resultCount = if (value.isNotEmpty()) dummylist.size else 0
                    }
                    if (query == "")
                        resultCount = 0

                    if (query == "") {
                        dummylist = listOg
                    } else {
                        dummylist = if (query != "") {
                            listOg.filter {
                                it.title.lowercase().contains(query) || it.description.lowercase().contains(query) || it.id
                                    .contains(query)
                            }
                        } else
                            listOg
                        resultCount = if (query != "") dummylist.size else 0
                    }
                    FAQIteration(
                        faqList = dummylist.sortedBy { it.date }.reversed(),
                        viewModel = viewModel,
                        navController
                    )

                }

                is Resource.Failure -> {
                    SnackBarCus(msg = it.errorMsgBody)
                }

                else -> {
                    ProgressBarIndicator()
                }
            }
        }
    }

}


@Composable
fun ErrorSolutionItem(sol: FAQAnsModel) {
    Card {
        Column {

        }
    }

}


@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ErrorSSView(list: List<String>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        list.size
    }
    var isShowDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(180.dp)
            .clickable {
                isShowDialog = !isShowDialog

            },
        colors = CardDefaults.cardColors(
            Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(.5.dp, getVerticalGradient())
    ) {
        if (list.size < 2) {

            Image(
                Cons.decodeImage(list[0]).asImageBitmap(), "",
                modifier = Modifier
                    .size(180.dp)
                    .weight(.5f),
                contentScale = ContentScale.Crop
            )

        } else if (list.size < 3) {
            val imgList = list.subList(0, 2)
            Column {
                Image(
                    Cons.decodeImage(imgList[0]).asImageBitmap(), "",
                    modifier = Modifier
                        .size(180.dp)
                        .weight(.5f),
                    contentScale = ContentScale.Crop
                )
                Image(
                    Cons.decodeImage(imgList[1]).asImageBitmap(), "",
                    modifier = Modifier
                        .size(180.dp)
                        .weight(.5f),
                    contentScale = ContentScale.Crop
                )
            }
        } else if (list.size < 4) {
            val imgList = list.subList(0, 3)
            Column {
                Row() {
                    Image(
                        Cons.decodeImage(imgList[0]).asImageBitmap(), "",
                        modifier = Modifier
                            .size(90.dp)
                            .weight(.5f),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        Cons.decodeImage(imgList[1]).asImageBitmap(), "",
                        modifier = Modifier
                            .size(90.dp)
                            .weight(.5f),
                        contentScale = ContentScale.Crop
                    )
                }
                Image(
                    Cons.decodeImage(imgList[2]).asImageBitmap(), "",
                    modifier = Modifier.width(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            val imgList = list.subList(0, 4)
            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .size(180.dp),
                    userScrollEnabled = false
                ) {
                    items(imgList) {
                        Image(
                            Cons.decodeImage(it).asImageBitmap(), "",
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Crop
                        )

                    }
                }
            }
        }

    }
    if (isShowDialog) {
        showDialog(pagerState, list) {
            isShowDialog = !isShowDialog
        }
    }
}



@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun showDialog(pagerState: PagerState, list: List<String>, onDismiss: () -> Unit) {
    // Show a dialog with a HorizontalPager
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Close button
            // ...
            var curPage by remember { mutableStateOf(0) }
            // Pager
            HorizontalPager(state = pagerState) { page ->
                val image = list[page]
                LaunchedEffect(key1 = true, block = {
                    curPage = list.indexOf(image)
                    Log.d("curpage", curPage.toString())
                    Log.d("page", page.toString())
                })
                ZoomableImage(image = list[page])

            }
            Spacer(Modifier.height(100.dp))
            Text(
                "${pagerState.currentPage + 1} out of ${list.size}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            // Indicators or any other UI elements
            // ...
        }
    }
}

@Composable
fun CodeViewText(code: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
//        .padding(horizontal = 10.dp)
            .height(110.dp),
        colors = CardDefaults.cardColors(
            Color.Black
        )

    ) {
        Box {
            val codeLines = code.trim().split("\n")
            Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .background(Color.Black)
                .padding(vertical = 10.dp)
                .padding(start = 5.dp ,end = 5.dp)) {
                codeLines.forEachIndexed { index, s ->
                    CodeLine(lineNo = index + 1, codeLine = s)
                }
            }

            val context = LocalContext.current
            Icon(painterResource(id = R.drawable.ic_copy),"",
                tint = WhiteColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(15.dp)
                    .clickable {// Get a reference to the ClipboardManager
                        // Get a reference to the ClipboardManager
                        val clipboardManager: ClipboardManager? =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                        val clipData = ClipData.newPlainText("code from ByteBuddy", code)
                        if (clipboardManager != null) {
                            clipboardManager.setPrimaryClip(clipData)
                            Toast
                                .makeText(context, "Code Copied Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
        }


    }
}

@Composable
fun CodeLine(lineNo:Int,codeLine : String) {
    Text(
        modifier = Modifier,
        text = "$lineNo $codeLine",
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        color = Color.White,
        softWrap = true
    )
}