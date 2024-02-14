package com.shakilpatel.notesapp.ui.main.chat.details

import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.common.tools.pdflib.size
import com.shakilpatel.notesapp.common.uicomponents.CircularImage
import com.shakilpatel.notesapp.common.uicomponents.OnNoDataFound
import com.shakilpatel.notesapp.common.uicomponents.SimpleTextField
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.social.ChatModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.main.chat.ChatViewModel
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailScreen(viewModel: ChatViewModel, navController: NavController) {
    BackHandler {
        viewModel.resetRecieverUser()
        viewModel.resetChats()
        navController.popBackStack()
    }
    ByteBuddyTheme {
        viewModel.recieverUser.collectAsState().value.let {
            when (it) {
                is Resource.Success -> {
                    LaunchedEffect(key1 = true) {
                        viewModel.getChats(it.result.uid)
                    }
                    val receiverUser = it.result
                    var msg by remember { mutableStateOf("") }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier =Modifier.fillMaxSize()
                        ) {
                            TopBarChatDetail(modifier = Modifier, user = it.result, navController)


                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                            ) {
                                viewModel.chats.collectAsState().value.let {
                                    when (it) {
                                        is Resource.Success -> {
                                            val coroutineScope = rememberCoroutineScope()

                                            val scrollState =
                                                rememberLazyListState(initialFirstVisibleItemIndex = it.result.size)
                                            val rootView = LocalView.current
//                                            DisposableEffect(Unit) {
//
//                                                val listener = ViewTreeObserver.OnPreDrawListener {
//                                                    coroutineScope.launch {
//                                                        scrollState.animateScrollToItem(it.result.size)
//                                                    }
//                                                    true
//                                                }
//
//                                                rootView.viewTreeObserver.addOnPreDrawListener(listener)
//
//                                                onDispose {
//                                                    rootView.viewTreeObserver.removeOnPreDrawListener(listener)
//                                                }
//                                            }


                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .focusable(true),
                                                state = scrollState
                                            ) {
                                                item {
                                                    if (it.result.isEmpty()) {
                                                        OnNoDataFound(msg = "No Message Yet...")
                                                    }
                                                }
                                                items(it.result.sortedBy { it.date }) {
                                                    ChatMessageCard(it, receiverUser.uid)
                                                }
                                                item{
                                                    Sp(h = 70.dp)
                                                }
                                            }
                                        }

                                        is Resource.Failure -> {}
                                        is Resource.Loading -> {
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }

                                        else -> {}
                                    }
                                }
                            }
                        }


                        Row(
                            modifier = Modifier

                                .background(WhiteColor)
                                .padding(horizontal = 5.dp)
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .padding(bottom = 10.dp)
                                .imePadding()
                                .align(Alignment.BottomCenter),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            Card(
                                modifier = Modifier.weight(.8f),
                                elevation = CardDefaults.cardElevation(8.dp),
                                shape = CircleShape
                            ) {
                                SimpleTextField(
                                    value = msg, onValueChange = {
                                        msg = it
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(45.dp)
                                        .focusable(true),
                                    maxLines = 6,
                                    placeholder = { Text("Enter Message") }
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Card(
                                colors = CardDefaults.cardColors(MainColor),
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(45.dp)
                                    .clickable {
                                        viewModel.sendMsg(
                                            msg = msg,
                                            recieverId = it.result.uid,
                                        )
                                        msg = ""
                                    },
                                elevation = CardDefaults.cardElevation(8.dp),

                                ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Send,
                                        "",
                                        tint = WhiteColor,
                                    )
                                }
                            }
                        }
                    }

                }

                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Failure -> {}
                else -> {}
            }
        }


    }
}

@Composable
fun ChatMessageCard(chat: ChatModel, rid: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {

        if (chat.receiverId != rid) {
            Column(
                modifier = Modifier
                    .widthIn(max = 200.dp, min = 80.dp)
                    .align(Alignment.CenterStart)
            ) {
                Card(
                    modifier = Modifier.widthIn(max = 200.dp, min = 80.dp),
                    shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 10.dp,
                        topStart = 10.dp,
                        bottomStart = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        MainColor.copy(
                            red = MainColor.red + 0.1f, // Increase or decrease the red value for brightness adjustment
                            green = MainColor.green - 0.1f, // Increase or decrease the green value for brightness adjustment
                            blue = MainColor.blue + 0.1f, // Increase or decrease the blue value for brightness adjustment
                            alpha = MainColor.alpha
                        )
                    )
                ) {
                    Text(
                        chat.message,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(top = 5.dp)
                            .padding(bottom = 3.dp),
                        color = WhiteColor
                    )
                    Text(
                        Cons.convertLongToDate(chat.date, "hh:mma"),
                        fontSize = 9.sp,
                        color = WhiteColor,
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .widthIn(max = 200.dp, min = 80.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Card(
                    modifier = Modifier.widthIn(max = 200.dp, min = 80.dp),
                    colors = CardDefaults.cardColors(
                        MainColor
                    ),
                    shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 0.dp,
                        topStart = 10.dp,
                        bottomStart = 10.dp
                    )
                ) {
                    Text(
                        text = chat.message,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(top = 5.dp)
                            .padding(bottom = 3.dp)
                    )

                    Text(
                        Cons.convertLongToDate(chat.date, "hh:mma"),
                        fontSize = 9.sp,
                        color = WhiteColor,
                        modifier = Modifier
                            .padding(end = 5.dp, bottom = 5.dp)
                            .widthIn(max = 200.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChatDetail(modifier: Modifier = Modifier, user: UserModel, navController: NavController) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp),
        title = {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Sp(w = 5.dp)
                CircularImage(
                    size = 50.dp,
                    image = user.profileImg ?: ""
                ) {

                }
                Sp(w = 10.dp)
                Column(Modifier.weight(.1f)) {
                    Text(
                        user.name.trim(),
                        color = WhiteColor
                    )
                    Text(
                        text = if (user.online) "Online" else "last seen at " + Cons.convertLongToDate(
                            user.lastSeen,
                            "hh:mma dd MMM"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 5.dp),
                        color = WhiteColor
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(top = 2.dp, end = 5.dp)
                        .clickable {

                        },
                    colors = CardDefaults.cardColors(WhiteColor)
                ) {
                    Text(
                        "View",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 3.dp
                        ),
                        color = MainColor
                    )
                }
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = WhiteColor,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(MainColor)
    )
}