package com.shakilpatel.bytebuddy.ui.main.chat.details

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.getRandomDarkColor
import com.shakilpatel.bytebuddy.common.uicomponents.CircularImage
import com.shakilpatel.bytebuddy.common.uicomponents.OnNoDataFound
import com.shakilpatel.bytebuddy.common.uicomponents.SimpleTextField
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.shimmerEffect
import com.shakilpatel.bytebuddy.data.models.social.ChatModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.data.notification.NotificationData
import com.shakilpatel.bytebuddy.data.notification.PushNotification
import com.shakilpatel.bytebuddy.ui.main.chat.ChatViewModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlin.random.Random

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
                    var isEditable by remember{ mutableStateOf(false) }
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
                                            var isDeleteConfirm by remember{ mutableStateOf(false) }
                                            var selectedCID by remember{ mutableStateOf("") }

                                            val scrollState =
                                                rememberLazyListState(initialFirstVisibleItemIndex = it.result.size)

                                            LaunchedEffect(key1 = it.result.size) {
                                                if(it.result.isNotEmpty()) {
                                                    scrollState.animateScrollToItem(it.result.size - 1)
                                                }
                                            }

                                            var col by remember{mutableStateOf(Color(0xFF000000))}
                                            var selectedMsgId by remember{mutableStateOf(ChatModel())}
                                            LaunchedEffect(key1 = true) {
                                                col = getRandomDarkColor()
                                            }
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
                                                    ChatMessageCard(it, receiverUser.uid,col,{
                                                        isDeleteConfirm = true
                                                        selectedCID = it.id
                                                    },
                                                        {
                                                            selectedCID = it.id
                                                            msg = it.message
                                                            isEditable = true
                                                            selectedMsgId = it.copy(it.message)
                                                        }
                                                    )

                                                }
                                                item{
                                                    Sp(h = 70.dp)
                                                }
                                            }
                                            if(isDeleteConfirm){
                                                AlertDialog(
                                                    onDismissRequest = {
                                                                       isDeleteConfirm = false
                                                    },
                                                    confirmButton = {
                                                        val context = LocalContext.current
                                                        Button(
                                                            onClick = {
                                                                viewModel.deleteMsg(selectedCID, context)
                                                                isDeleteConfirm = false
                                                            }
                                                        ) {
                                                            Text(text = "Confirm")
                                                        } },
                                                    dismissButton = {
                                                        Button(
                                                            onClick = {
                                                                isDeleteConfirm = false
                                                            }
                                                        ) {
                                                            Text(text = "Cancel")
                                                        }
                                                    }   ,
                                                    title = {

                                                            Column(
                                                                modifier = Modifier
                                                                    .fillMaxWidth(),
                                                            ){
                                                                Text(
                                                                    text = "Are you sure want to delete ?")
                                                                Sp(h = 10.dp)
                                                                Text (
                                                                    "Message : \'${it.result.find { it.id == selectedCID }?.message}\'",
                                                                    style = MaterialTheme.typography.bodyMedium
                                                                )
                                                                Text(
                                                                    text = "Time : ${Cons.convertLongToDate(it.result.find { it.id == selectedCID }?.date!!,"hh:mm a")}",
                                                                    style = MaterialTheme.typography.bodyMedium
                                                                )
                                                            }
                                                    }
                                                )
                                            }
                                            if(isEditable){
                                                Dialog(
                                                    onDismissRequest = {
                                                        isEditable = false
                                                    }
                                                ){

                                                    Card(
                                                        modifier =Modifier.fillMaxWidth()
                                                    ){
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(
                                                                    20.dp
                                                                )
                                                        ){
                                                            SimpleTextField(
                                                                value = msg,
                                                                onValueChange = {
                                                                    msg = it
                                                                },
                                                                modifier = Modifier.weight(.7f).height(45.dp)
                                                            )

                                                            Card(
                                                                colors = CardDefaults.cardColors(MainColor),
                                                                shape = CircleShape,
                                                                modifier = Modifier
                                                                    .size(45.dp)
                                                                    .clickable {
                                                                        if (msg.isNotEmpty()) {
                                                                            viewModel.editMsg(
                                                                                msg =  selectedMsgId.copy(id = selectedCID,message = msg),
                                                                            selectedCID
                                                                                )
                                                                            msg = ""
                                                                            isEditable= false
                                                                            selectedMsgId = ChatModel()
                                                                        }
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
                                            }

                                        }

                                        is Resource.Failure -> {}
                                        is Resource.Loading -> {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .verticalScroll(rememberScrollState()),
                                            ) {
                                                repeat(25){
                                                    ChatLoad(Random.nextBoolean())
                                                }

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

                                    val context = LocalContext.current
                                    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                                    val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                                    recognizerIntent.putExtra(
                                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                    )
                                    val speechTextState = remember { mutableStateOf(TextFieldValue()) }

                                    val speechRecognitionLauncher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.RequestPermission(),
                                        onResult = { isGranted: Boolean ->
                                            if (isGranted) {
                                                speechRecognizer.startListening(recognizerIntent)
                                            }
                                        }
                                    )
                                    SimpleTextField(
                                        value = msg, onValueChange = {
                                            msg = it
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(45.dp)
                                            .focusable(true),
                                        maxLines = 6,
                                        placeholder = { Text("Enter Message") },
                                        trailingIcon = {
                                            Icon(
                                                painterResource(R.drawable.ic_mic),
                                                "",
                                                modifier = Modifier.clickable{
                                                    if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                                        speechRecognizer.startListening(recognizerIntent)
                                                    } else {
                                                        speechRecognitionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                                    }
                                                }
                                            )
                                        }
                                    )
                                    speechRecognizer.setRecognitionListener(object : RecognitionListener {
                                        override fun onReadyForSpeech(params: Bundle?) {}
                                        override fun onBeginningOfSpeech() {}
                                        override fun onRmsChanged(rmsdB: Float) {}
                                        override fun onBufferReceived(buffer: ByteArray?) {}
                                        override fun onEndOfSpeech() {}
                                        override fun onError(error: Int) {}
                                        override fun onResults(results: Bundle?) {
                                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                            if (!matches.isNullOrEmpty()) {
                                                speechTextState.value = TextFieldValue(matches[0])
                                                msg = speechTextState.value.text
                                            }
                                        }
                                        override fun onPartialResults(partialResults: Bundle?) {
                                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                            if (!matches.isNullOrEmpty()) {
                                                speechTextState.value = TextFieldValue(matches[0])
                                                msg = speechTextState.value.text
                                            }
                                        }
                                        override fun onEvent(eventType: Int, params: Bundle?) {}
                                    })
                                }

                                Spacer(modifier = Modifier.width(5.dp))
                                val view = LocalView.current
                                Card(
                                    colors = CardDefaults.cardColors(MainColor),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clickable {
                                            if (msg.isNotEmpty()) {
                                                viewModel.sendMsg(
                                                    msg = msg.trim(),
                                                    recieverId = it.result.uid,
                                                )

                                                view.vibrate()

                                                if (!receiverUser.online) {
                                                    com.shakilpatel.bytebuddy.data.notification.Cons.sendNotification(
                                                        PushNotification(
                                                            NotificationData(
                                                                "",
                                                                msg,
                                                                time = System.currentTimeMillis(),
                                                                uid = FirebaseAuth.getInstance().uid!!,
                                                                type = "msg"
                                                            ),
                                                            receiverUser.token,
                                                            ismsg = "true"
                                                        )
                                                    )
                                                }
                                                msg = ""
                                            }
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
fun ChatLoad(isSend : Boolean= true) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val width = Random.nextInt(from = 80, until = 300).dp
        if (isSend) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(width)
                    .height(32.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 15.dp,
                            bottomEnd = 0.dp,
                            topStart = 15.dp,
                            bottomStart = 15.dp
                        )
                    )
                    .shimmerEffect()

            )
        }else{

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(width)
                    .height(32.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 15.dp,
                            bottomEnd = 15.dp,
                            topStart = 0.dp,
                            bottomStart = 15.dp
                        )
                    )
                    .shimmerEffect()

            )
        }
    }
}
fun View.vibrate() = reallyPerformHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
fun View.vibrateStrong() = reallyPerformHapticFeedback(HapticFeedbackConstants.LONG_PRESS)

private fun View.reallyPerformHapticFeedback(feedbackConstant: Int) {
    if (context.isTouchExplorationEnabled()) {
        // Don't mess with a blind person's vibrations
        return
    }
    // Either this needs to be set to true, or android:hapticFeedbackEnabled="true" needs to be set in XML
    isHapticFeedbackEnabled = true

    // Most of the constants are off by default: for example, clicking on a button doesn't cause the phone to vibrate anymore
    // if we still want to access this vibration, we'll have to ignore the global settings on that.
    performHapticFeedback(feedbackConstant, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
}

private fun Context.isTouchExplorationEnabled(): Boolean {
    // can be null during unit tests
    val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
    return accessibilityManager?.isTouchExplorationEnabled ?: false
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatMessageCard(chat: ChatModel, rid: String,col : Color,onDeleteMsg : ()->Unit,onDoubleTap:()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val sShape = RoundedCornerShape(
            topEnd = 15.dp,
            bottomEnd = 0.dp,
            topStart = 15.dp,
            bottomStart = 15.dp
        )
        val rShape = RoundedCornerShape(
            topEnd = 15.dp,
            bottomEnd = 15.dp,
            topStart = 0.dp,
            bottomStart = 15.dp
        )

        if (chat.receiverId != rid) {
            Column(
                modifier = Modifier
                    .shadow(
                        2.dp, rShape
                    )
                    .widthIn(max = 300.dp, min = 80.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        col, rShape
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
        } else {
            val view = LocalView.current
            Column(
                modifier = Modifier
                    .widthIn(max = 300.dp, min = 80.dp)
                    .align(Alignment.CenterEnd)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                // perform some action here..
                                view.vibrateStrong()
                                onDeleteMsg()
                            },
                            onDoubleTap = {
                                onDoubleTap()
                            }
                        )
                    }
            ) {
                Box (
                    modifier = Modifier
                        .shadow(
                            2.dp, sShape
                        )
                        .widthIn(max = 300.dp, min = 80.dp)
                        .background(
                            MainColor, sShape
                        )
                        .padding(5.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = chat.message,
                        modifier = Modifier
//                            .align(Alignment.CenterStart)
                            .padding(horizontal = 10.dp)
                            .padding(top = 5.dp)
                            .padding(bottom = 3.dp, end = 40.dp),
                        color = WhiteColor,
                        softWrap = true
                    )

                    Text(
                        Cons.convertLongToDate(chat.date, "hh:mma"),
                        fontSize = 9.sp,
                        color = WhiteColor,
                        modifier = Modifier
                            .padding(end = 5.dp, bottom = 5.dp)
                            .align(Alignment.BottomEnd)
                        ,
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

                if(!user.profileImg.isNullOrEmpty()) {
                    Sp(w = 5.dp)
                    CircularImage(
                        size = 50.dp,
                        image = user.profileImg ?: ""
                    ) {

                    }
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
                            navController.navigate(Screen.UsersProfile.route + "/${user.uid}")
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