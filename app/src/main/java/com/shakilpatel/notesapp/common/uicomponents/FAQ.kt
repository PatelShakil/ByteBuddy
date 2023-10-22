package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.TextColor
import com.shakilpatel.notesapp.data.models.error.FAQAnsModel
import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.main.feed.error.CodeViewText
import com.shakilpatel.notesapp.ui.main.feed.error.ErrorSSView
import com.shakilpatel.notesapp.ui.main.feed.error.FAQViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FAQItem(
    faq: FAQModel,
    viewModel: FAQViewModel
) {
    var user by remember { mutableStateOf(UserModel()) }
    var isSaved by remember { mutableStateOf(false) }
    var isPostAnsClick by remember { mutableStateOf(false) }
    viewModel.getUserModel(faq.userId) {
        user = it
    }
    viewModel.getUserModel {
        isSaved = it.saved.errors.contains(faq.id)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(.95f)
            .fillMaxHeight(.95f),
        border = BorderStroke(.5.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "#${faq.id}",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .weight(.6f),
                    textAlign = TextAlign.Start
                )
                Text(Cons.convertLongToDate(faq.date, "hh:mma dd/MMM"), fontSize = 9.sp)
            }
            Sp(h = 5.dp)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    MainColor
                ),
                elevation = CardDefaults.cardElevation(
                    4.dp
                )
            ) {
                AnimatedVisibility(visible = user != UserModel()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Sp(w = 10.dp)
                        CircularImage(size = 45.dp, image = user.profileImg) {

                        }
                        Sp(w = 5.dp)
                        Text(
                            text = user.name, style = MaterialTheme.typography.titleSmall,
                            fontSize = 14.sp,
                            color = TextColor
                        )
                    }
                }
            }
            Column(modifier = Modifier.fillMaxWidth(.95f)) {
                Text(
                    faq.title, style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp
                )
                var isDescExpand by remember { mutableStateOf(false) }
                if (isDescExpand) {
                    faq.description.lines().forEach {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            softWrap = true,
                            modifier = Modifier.fillMaxWidth(.95f)
                        )
                    }
                } else {
                    faq.description.lines().take(5).forEach {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            softWrap = true,
                            modifier = Modifier.fillMaxWidth(.95f)
                        )
                    }
                }
                if (faq.description.lines().size > 5) {
                    if (!isDescExpand && faq.description.lines().size > 5) {
                        Text("View More",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 9.sp,
                            modifier = Modifier
                                .fillMaxWidth(.95f)
                                .clickable {
                                    isDescExpand = !isDescExpand
                                })
                    } else {
                        Text("View Less",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 9.sp,
                            modifier = Modifier
                                .fillMaxWidth(.95f)
                                .clickable {
                                    isDescExpand = !isDescExpand
                                })
                    }
                }

            }
            Sp(h = 5.dp)
            AnimatedVisibility(faq.code.isNotEmpty()) {
                CodeViewText(code = faq.code)
            }
            Sp(h = 5.dp)
            //post answer button
            if (!faq.screenshots.isNullOrEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ErrorSSView(list = faq.screenshots)
                    Sp(w = 10.dp)
                    Column(
                        modifier = Modifier.weight(.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
//                            onPostAnswerClick(error)
                            isPostAnsClick = !isPostAnsClick

                        }, modifier = Modifier) {
                            Text("Post Answer")
                        }
                        Sp(h = 10.dp)
                        Icon(
                            painterResource(if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                            "",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    viewModel.saveError(faq.id)
                                },
                            tint = MainColor
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(.95f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        isPostAnsClick = !isPostAnsClick

                    }, modifier = Modifier.weight(.8f)) {
                        Text("Post Answer")
                    }
                    Sp(w = 10.dp)
                    Icon(
                        painterResource(if (isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                        "",
                        modifier = Modifier
                            .clickable {
                                viewModel.saveError(faq.id)
                            },
                        tint = MainColor
                    )
                }
            }

            //answers
            if (faq.answers.isNotEmpty()) {
                Sp(h = 10.dp)
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(.5.dp), color = Gray
                )
                Sp(h = 15.dp)
                var isAnswersOpen by remember { mutableStateOf(false) }
                AnimatedVisibility(visible = isAnswersOpen) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(faq.answers.sortedBy { it.date }.reversed()) {
                            FAQAnsItem(ans = it, viewModel = viewModel)

                        }
                    }
                }
                if (!isAnswersOpen) {
                    ViewAnswersItemCard {
                        isAnswersOpen = !isAnswersOpen
                    }
                }
            } else {
                Sp(h = 10.dp)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CustomOutlinedButton(label = "Become the first one to answer") {
                        isPostAnsClick = !isPostAnsClick
                    }
                }
                Sp(h = 5.dp)
            }

        }
    }
    if (isPostAnsClick) {
        FAQAnsDialog(faq, viewModel = viewModel) {
            isPostAnsClick = !isPostAnsClick
        }
    }
}

@Composable
fun ViewAnswersItemCard(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .clickable {
                    onClick()
                },
            colors = CardDefaults.cardColors(
                MainColor
            ),
            elevation = CardDefaults.cardElevation(
                18.dp
            ),
            shape = RoundedCornerShape(50.dp)
        ) {

            Text(
                "View Answers",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center,
                color = TextColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FAQAnsDialog(faq: FAQModel, viewModel: FAQViewModel, onDismiss: () -> Unit) {
    var user by remember { mutableStateOf(UserModel()) }
    var curUser by remember { mutableStateOf(UserModel()) }
    viewModel.getUserModel(faq.userId) {
        user = it
    }
    viewModel.getUserModel {
        curUser = it
    }
    if (user != UserModel()) {
        ModalBottomSheet(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HorizontalBrush)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Sp(h = 10.dp)
                    Text(
                        "Upload your Answer",
                        style = MaterialTheme.typography.titleLarge,
                        color = MainColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Sp(h = 10.dp)

                    Text(
                        user.name + "'s Que. : " + faq.title,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleSmall,
                        softWrap = true
                    )


                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var aMessage by remember { mutableStateOf("") }
                        var aCode by remember { mutableStateOf("") }
                        CustomTextField(
                            value = aMessage,
                            label = "Message*",
                            hint = "Enter your valuable opinion",
                            onTextChanged = {
                                aMessage = it
                            })
                        Sp(h = 5.dp)
                        CustomTextField(
                            value = aCode,
                            label = "Code",
                            hint = "if you have any code then paste it here",
                            onTextChanged = {
                                aCode = it
                            })
                        Sp(h = 15.dp)
                        AnimatedVisibility(visible = aMessage.isNotEmpty()) {
                            CustomOutlinedButton(label = "Upload") {
                                viewModel.uploadFAQAns(
                                    FAQAnsModel(
                                        Cons.generateRandomValue(9),
                                        faq.id,
                                        aMessage.trim(),
                                        aCode.trim(),
                                        System.currentTimeMillis(),
                                        curUser.uid,
                                        emptyList()
                                    )
                                )
                                onDismiss()

                            }
                        }


                    }
                    Sp(h = 180.dp)

                }
            }

        }
    }
}

@Composable
fun FAQAnsItem(ans: FAQAnsModel, viewModel: FAQViewModel) {
    var user by remember { mutableStateOf(UserModel()) }
    var curUser by remember { mutableStateOf(UserModel()) }
    var isUpvote by remember { mutableStateOf(false) }
    var isDownvote by remember { mutableStateOf(false) }
    viewModel.getUserModel {
        curUser = it
        if (ans.voteList.filter { user -> user.userId == it.uid }.isNotEmpty()) {
            isUpvote = ans.voteList.filter { user -> user.isUp && user.userId.contains(it.uid) }
                .isNotEmpty()
            isDownvote = ans.voteList.filter { user -> !user.isUp && user.userId.contains(it.uid) }
                .isNotEmpty()
        }
    }
    viewModel.getUserModel(ans.userId) {
        user = it
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(
            modifier = Modifier.weight(.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UpVoteBox(selected = isUpvote, ans.voteList.filter { it.isUp }.size) {
                //on upvote
                viewModel.onUpVote(ans)
            }
            Sp(h = 2.dp)
            DownVoteBox(selected = isDownvote, ans.voteList.filter { !it.isUp }.size) {
                //on downvote
                viewModel.onDownVote(ans)
            }
        }
        Column(modifier = Modifier.weight(.85f)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                CircularImage(size = 40.dp, image = user.profileImg) {

                }
                Sp(w = 5.dp)
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(user.name, fontSize = 10.sp, modifier = Modifier.weight(.6f))
                        Text(Cons.convertLongToDate(ans.date, "hh:mma dd/MMM"), fontSize = 8.sp)
                    }
                    Text(ans.message, style = MaterialTheme.typography.bodySmall)

                }
            }
            Sp(h = 5.dp)
            if (ans.code.isNotEmpty())
                CodeViewText(code = ans.code)
            Sp(h = 5.dp)
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(.5.dp), color = LightGray
            )
            Sp(h = 5.dp)
        }
    }

}

@Composable
fun UpVoteBox(selected: Boolean, count: Int, onClick: () -> Unit) {
    val iconColor = if (selected) White else MainColor
    val bgColor = if (selected) MainColor else White
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick()
        }) {
        Text(count.toString(), color = MainColor, style = MaterialTheme.typography.titleSmall)
        Sp(w = 3.dp)
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(.5.dp, MainColor, CircleShape)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, "", tint = iconColor)
        }
    }
}

@Composable
fun DownVoteBox(selected: Boolean, count: Int, onClick: () -> Unit) {
    val iconColor = if (selected) White else MainColor
    val bgColor = if (selected) MainColor else White
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick()
        }) {
        Text(count.toString(), color = MainColor, style = MaterialTheme.typography.titleSmall)
        Sp(w = 3.dp)
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(.5.dp, MainColor, CircleShape)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.KeyboardArrowDown, "", tint = iconColor)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FAQIteration(faqList: List<FAQModel>, viewModel: FAQViewModel) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        faqList.size
    }
    Column {
        if (faqList.isEmpty()) {
            OnNoDataFound(msg = "No such faq found")
        }
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
        ) { page ->
            Sp(h = 10.dp)
            FAQItem(faq = faqList[page], viewModel)
            Sp(h = 5.dp)
        }
//                        LazyColumn(
//                            horizontalAlignment = CenterHorizontally,
//                            modifier = Modifier.fillMaxWidth(),
//                            flingBehavior = ScrollableDefaults.flingBehavior()
//                        ) {
//                            items(it.result) {
//                                FAQItem(it, viewModel)
//                                Sp(h = 5.dp)
//                                viewModel.errorSaveResult.collectAsState().value.let {
//                                    when (it) {
//                                        is Resource.Loading -> {
//
//                                        }
//
//                                        is Resource.Success -> {
//                                            Log.d("Saved Result Success", it.result.toString())
//                                        }
//
//                                        is Resource.Failure -> {
//                                            Log.d("Saved Result Failure", it.errorMsgBody)
//                                        }
//
//                                        else -> {}
//                                    }
//                                }
//                            }
//                        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FAQIterationStr(faqList: List<String>, viewModel: FAQViewModel) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        faqList.size
    }
    Column {
        if (faqList.isEmpty()) {
            OnNoDataFound(msg = "No such faq found")
        }
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
        ) { page ->
            Sp(h = 10.dp)
            val error = remember { mutableStateOf(FAQModel()) }
            viewModel.getError(faqList[page]) {
                error.value = it
            }
            if (error.value != FAQModel()) {
                FAQItem(
                    faq = error.value,
                    viewModel = viewModel
                )
            } else
                ProgressBarIndicator()
            Sp(h = 5.dp)
        }
//                        LazyColumn(
//                            horizontalAlignment = CenterHorizontally,
//                            modifier = Modifier.fillMaxWidth(),
//                            flingBehavior = ScrollableDefaults.flingBehavior()
//                        ) {
//                            items(it.result) {
//                                FAQItem(it, viewModel)
//                                Sp(h = 5.dp)
//                                viewModel.errorSaveResult.collectAsState().value.let {
//                                    when (it) {
//                                        is Resource.Loading -> {
//
//                                        }
//
//                                        is Resource.Success -> {
//                                            Log.d("Saved Result Success", it.result.toString())
//                                        }
//
//                                        is Resource.Failure -> {
//                                            Log.d("Saved Result Failure", it.errorMsgBody)
//                                        }
//
//                                        else -> {}
//                                    }
//                                }
//                            }
//                        }

    }
}

