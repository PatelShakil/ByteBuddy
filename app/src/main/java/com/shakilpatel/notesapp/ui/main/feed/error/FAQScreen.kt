@file:OptIn(ExperimentalMaterial3Api::class)

package com.shakilpatel.notesapp.ui.main.feed.error

import android.util.Log
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.pager.PagerDefaults
//import com.google.accompanist.pager.VerticalPager
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.getVerticalGradient
import com.shakilpatel.notesapp.common.uicomponents.FAQAppBar
import com.shakilpatel.notesapp.common.uicomponents.FAQIteration
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.notesapp.common.uicomponents.SnackBarCus
import com.shakilpatel.notesapp.common.uicomponents.ZoomableImage
import com.shakilpatel.notesapp.data.models.error.FAQAnsModel
import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme


@Composable
fun FaqScreen(viewModel: FAQViewModel, navController: NavController) {
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
                    var query by remember { mutableStateOf("") }
                    FAQAppBar(title = "FAQ", resultCount, navController = navController) { value ->
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
                                it.title.lowercase().contains(query) || it.description.lowercase()
                                    .contains(query)
                            }
                        } else
                            listOg
                        resultCount = if (query != "") dummylist.size else 0
                    }
                    FAQIteration(
                        faqList = dummylist.sortedBy { it.date }.reversed(),
                        viewModel = viewModel
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

@Composable
fun ESSCardItem(image: String, onClick: () -> Unit) {
    Card(modifier = Modifier.size(100.dp)) {
        if (image.isNotEmpty()) {
            // Display the image
            Image(
                Cons.decodeImage(image).asImageBitmap(),
                contentDescription = null, // Provide a meaningful description
                contentScale = ContentScale.Crop
            )
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
            .height(110.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState())
                .height(110.dp)
                .background(Color.Black)
                .padding(10.dp),
            text = code,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = Color.White,
            softWrap = true
        )

    }
}

@Composable
fun CodeView(code: String) {
    val codeLines = code.trim().split("\n")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .horizontalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            for (line in codeLines) {
                CodeLine(line)
            }
        }
    }
}

@Composable
fun CodeLine(line: String) {
    val keywords = setOf("fun", "val", "var", "if", "else", "for", "while")

    val operators = setOf("+", "-", "*", "/", "=", "==", "!=")
    val fontFamily = FontFamily.Monospace

    val annotatedString = AnnotatedString.Builder()
    var currentPosition = 0

    while (currentPosition < line.length) {
        val word = getNextWord(line, currentPosition)
        val style = when {
            keywords.contains(word) -> SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)
            operators.contains(word) -> SpanStyle(color = Color.Green)
            else -> SpanStyle(color = Color.Black)
        }

        annotatedString.append(
            AnnotatedString(
                text = word,
                spanStyle = style
            )
        )

        currentPosition += word.length
    }

    Box(
        modifier = Modifier.padding(vertical = 2.dp),
        content = {
            Text(
                text = annotatedString.toAnnotatedString(),
                fontFamily = fontFamily,
                fontSize = 12.sp
            )
        }
    )
}


fun getNextWord(line: String, currentPosition: Int): String {
    var endPosition = currentPosition

    while (endPosition < line.length && line[endPosition].isLetterOrDigit()) {
        endPosition++
    }

    return line.substring(currentPosition, endPosition)
}
