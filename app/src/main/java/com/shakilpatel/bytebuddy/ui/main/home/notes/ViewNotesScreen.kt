package com.shakilpatel.bytebuddy.ui.main.home.notes

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.easy.translator.EasyTranslator
import com.easy.translator.LanguagesModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.shakilpatel.bytebuddy.common.tools.pdflib.ResourceType
import com.shakilpatel.bytebuddy.common.tools.pdflib.VerticalPDFReader
import com.shakilpatel.bytebuddy.common.tools.pdflib.rememberVerticalPdfReaderState
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.TextColor
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.getHorizontalGradient
import com.shakilpatel.bytebuddy.common.getVerticalGradient
import com.shakilpatel.bytebuddy.common.uicomponents.ConfirmationDialog
import com.shakilpatel.bytebuddy.common.uicomponents.OnNoDataFound
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.SearchBar
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.TranslateDialog
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.learning.NotesPage
import com.shakilpatel.bytebuddy.ui.main.create.TabIndicator
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ViewNotesScreen(notesId: String, viewModel: NotesViewModel, navController: NavController) {
    ByteBuddyTheme {
        Column {
            HideSystemBars()
            val isRotationChange by rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(isRotationChange) {
                Log.d("TAG", "ViewNotesScreen: $notesId")
//                viewModel.getNote(notesId)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f
                ) {
                    // provide pageCount
                    if (viewModel.curNote.value.text.isEmpty()) 1 else 2
                }
                Column {
                    Tabs(
                        pagerState = pagerState, modifier = Modifier.height(40.dp),
                        if (viewModel.curNote.value.text.isEmpty()) listOf("PDF")
                        else listOf("Text", "PDF")
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TabsContent(pagerState = pagerState, viewModel, navController)
                    }

                }
            }
        }
    }
}

@Composable
fun CheckButton(title:String,isSelected : Boolean, onClick:()->Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) MainColor else Color.White, RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
    contentAlignment = Alignment.Center
    ){
        Text(title,
            color = if(isSelected) WhiteColor else MainColor,
            modifier = Modifier.padding(horizontal = 15.dp,vertical = 2.dp),
            style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun HideSystemBars() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.findActivity()?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun Tabs(
    pagerState: PagerState,
    modifier: Modifier,
    list: List<String>
) {
    val scope = rememberCoroutineScope()
    androidx.compose.material.TabRow(
        modifier = modifier,
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabIndicator(pagerState = pagerState, tabPositions = tabPositions)
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) Color.White else Color.Black,
                        modifier = Modifier
                            .background(
                                if (pagerState.currentPage == index) MainColor else Color.White,
                                RoundedCornerShape(15.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 15.dp)
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, viewModel: NotesViewModel, navController: NavController) {
    viewModel.notes.collectAsState().value.let {
        when (it) {
            is Resource.Success -> {
                viewModel.curNote.value = it.result

                HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                    when (page) {
                            0 -> {
                                TextScreen(it.result,viewModel = viewModel)

                            }

                            1 -> {
                                PDFScreen(viewModel = viewModel, navController)
//                        val context = LocalContext.current
//                        context.startActivity(PdfViewerActivity.launchPdfFromUrl(
//                            context,
//                            notes.pdfFile,
//                            notes.title,
//                            "Downloads",
//                            false
//                        ))
                            }

                    }
                }
            }

            else -> {}
        }
    }

}

/*
class RetrievePDFfromUrl() : AsyncTask<String, Void, InputStream>() {
    override fun doInBackground(vararg strings: String?): InputStream? {
        // We are using an InputStream
        // for getting our PDF.
        var inputStream: InputStream? = null
        try {
            val url = URL(strings[0]) // Assuming the URL is passed as the first argument.
            // Below is the step where we are
            // creating our connection.
            val urlConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.responseCode == 200) {
                // Response is successful.
                // We are getting an InputStream from the URL
                // and storing it in our variable.
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        } catch (e: IOException) {
            // This is the method
            // to handle errors.
            e.printStackTrace()
        }
        return inputStream
    }

    override fun onPostExecute(inputStream: InputStream?) {
        // After the execution of our async task,
        // we are loading our PDF in our PDF view.
        if (inputStream != null) {
            // Assuming you have a PDF viewer or a
            // PDF rendering component.
            // Replace 'YourPDFViewer' with the actual PDF viewer component.
        }
        // Dismiss any progress dialog or loading indicator here.
        // pd.dismiss()
    }

    fun onExecute(onExe: (InputStream) -> Unit) {

    }
}
*/

@Composable
fun PDFScreen(viewModel: NotesViewModel, navController: NavController) {

    val notes = viewModel.curNote.value
    val pdfState = rememberVerticalPdfReaderState(resource = ResourceType.Remote(notes.pdfFile),
        isZoomEnable = true)
    if (pdfState.error != null)
        SnackBarCus(msg = pdfState.error!!.localizedMessage)
    Log.d("Percent", pdfState.loadPercent.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HorizontalBrush)
    ) {
        Text(
            notes.title,
            style = MaterialTheme.typography.titleSmall,
            softWrap = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(.95f)
        )
        Box(modifier = Modifier.fillMaxSize()) {
//                        AndroidView(
//                            modifier = Modifier
//                                .fillMaxSize(),
//                            factory = { context ->
//                                PDFView(context, null).apply {
//
//                                    fromStream(RetrievePDFfromUrl().execute(notes.pdfFile).get())
//                                        .load()
//                                }
//                            })


            var isConfirmCancel by remember {
                mutableStateOf(false)
            }


            if(!pdfState.isLoaded){
                ProgressBarCus(onDismiss = {
                                           isConfirmCancel = true
                }, progress = pdfState.loadPercent / 100.toFloat())
            }
            pdfState.changeZoomState(true)

                Box(modifier = Modifier.fillMaxWidth()) {
                    VerticalPDFReader(
                        state = pdfState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(HorizontalBrush)
                    )
                }


            if (isConfirmCancel) {
                ConfirmationDialog(msg = "Are you want to cancel loading PDF ?", onDismiss = {
                    isConfirmCancel = !isConfirmCancel
                }) {
                    isConfirmCancel = !isConfirmCancel
                    pdfState.close()
                    navController.popBackStack()
                }
            }
        }
    }
}


@Composable
fun TextScreen(note:NotesModel,viewModel: NotesViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HorizontalBrush)
    ) {
        var textList by remember { mutableStateOf(listOf<NotesPage>()) }
        var textListOg by remember { mutableStateOf(listOf<NotesPage>()) }
        LaunchedEffect(key1 = true, block = {
            textList = note.text
            textListOg = note.text
            Log.d("TextList", textListOg.toString())
        })
        val listState = rememberLazyListState()
        var resultCount by remember { mutableIntStateOf(0) }
        var query by remember { mutableStateOf("") }
        var isSearchBarVisible by remember { mutableStateOf(true) }
        Column {
                SearchBar(
                    hint = "Search in ${note.title}",
                    onTextChanged = { value ->
                        query = value
                        textList = if (value.isNotEmpty()) {
                            textListOg.filter { it.text.lowercase().contains(value) }
                        } else
                            textListOg
                        resultCount = if (value.isNotEmpty()) textList.size else 0
                    }
                )
                if (resultCount > 0) {
                    Text(
                        text = "'$query' found in $resultCount pages. ",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MainColor)
                            .padding(bottom = 5.dp),
                        textAlign = TextAlign.Center,
                        color = TextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TransItems(
                    modifier = Modifier,
                    state = listState,
                    list = textList,
                    viewModel = viewModel
                )
        }
    }

}

@Composable
fun TransItems(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    list: List<NotesPage>, viewModel: NotesViewModel
) {
    AnimatedVisibility(visible = list.isEmpty()) {
        OnNoDataFound(msg = "Text not found in this note")
    }
    AnimatedVisibility(visible = list.isNotEmpty()) {
        var fontSize by remember { mutableStateOf(14.sp) }
        val minFontSize = 9.sp
        val maxFontSize = 42.sp
        LazyColumn(
            state = state,
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, rotation ->
                        // Adjust font size with zoom, but limit it within the specified range
                        fontSize *= zoom
//                        fontSize *= if (fontSize < 42.sp && fontSize > 9.sp) zoom else 1f
//                        fontSize =
//                            fontSize.value
//                                .coerceIn(minFontSize.value, maxFontSize.value)
//                                .toSp()
                        Log.d("FONT SIZE", fontSize.toString())
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            items(list) { notes ->
                if (notes.text.trim() != "")
                    TranslatePageItem(
                        notes = notes,
                        if (fontSize > 42.sp) 42.sp else if (fontSize < 9.sp) 9.sp else fontSize,
                        viewModel
                    )
            }
            item {
                Sp(h = 200.dp)
            }
        }
    }

}

@Composable
fun TranslatePageItem(notes: NotesPage, fontSize: TextUnit, viewModel: NotesViewModel) {
    val curNote by remember { mutableStateOf(notes) }
    val context = LocalContext.current
    var isTranslateClick by remember { mutableStateOf(false) }
    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getHorizontalGradient())
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sp(h = 10.dp)
                ZoomableText(notes.text, fontSize)
//                Text(
//                    notes.text, softWrap = true,
//                    color = TextColor,
//                )
                Sp(h = 5.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(.9f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            if (viewModel.textToSpeech?.isSpeaking == true)
                                viewModel.terminate()
                            else
                                viewModel.speak(notes.text)

                        },
                        modifier = Modifier
                            .clip(CircleShape)
                    ) {
                        Icon(
                            painterResource(id = if (viewModel.textToSpeech?.isSpeaking == true) R.drawable.ic_mic_off else R.drawable.ic_mic),
                            "",
                            tint = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.terminate()
                            isTranslateClick = true
                        },
                        modifier = Modifier
                            .clip(CircleShape),
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_translate), "",
                            tint = Color.White
                        )
                    }

                }
            }
        }
        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 3.dp)
                    .clip(CircleShape)
                    .background(getVerticalGradient()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    notes.pageNo.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextColor
                )
            }
        }
        val isTranslateProgress = viewModel.isProgressRun
        var notesErrorMsg by remember { mutableStateOf("") }
        var isConfirmCancelTranslation by remember { mutableStateOf(false) }
        val translator = EasyTranslator(context)
        val scope = rememberCoroutineScope()
        if (isTranslateClick) {
            TranslateDialog(onSuccess = { data ->
                if (translator.getLanguagesList().find { it.shortCode == data.id } != null) {
                    scope.launch {
                        translator.translate(
                            notes.text,
                            LanguagesModel.AUTO_DETECT,
                            translator.getLanguagesList().find { it.shortCode == data.id }!!,
                            {
                                isTranslateClick = false
                                curNote.text = it
                            }, {
                                notesErrorMsg = it
                                isTranslateClick = false
                                Log.d("TransError", it)
                            },
                            20000
                        )
                    }
                }
            }) {
                isConfirmCancelTranslation = true
            }
        }
        if (isConfirmCancelTranslation) {
            ConfirmationDialog(msg = "Are you sure want to cancel translation?", onDismiss = {
                isConfirmCancelTranslation = false
            }) {
                translator.stopLoading()
                isTranslateClick = false
                isConfirmCancelTranslation = false
            }
        }
        if (notesErrorMsg != "") {
            SnackBarCus(msg = "Try Again...")
        }
        if (isTranslateProgress.value) {
            viewModel.terminate()
            ProgressBarCus(onDismiss = { /*TODO*/ })
        }

    }

}

@Composable
fun ZoomableText(text: String, fontSize: TextUnit) {
//    var fontSize by remember { mutableStateOf(12.sp) }
    Text(
        text,
        fontSize = fontSize,
        softWrap = true,
        color = TextColor,
        modifier = Modifier
            .fillMaxWidth()
    )
}
