package com.shakilpatel.bytebuddy.ui.main.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.shakilpatel.bytebuddy.ui.main.feed.error.FaqScreen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun FeedScreen(faqId : String,navController: NavController, onBack: () -> Unit) {
//    BackHandler {
//        onBack()
//    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        1
    }
    ByteBuddyTheme {
        Column(modifier = Modifier.fillMaxSize()) {

            FaqScreen(faqId,viewModel = hiltViewModel(), navController = navController)
//            Tabs(pagerState = pagerState, modifier = Modifier, list = listOf("Faq"))
//            Box(modifier = Modifier.fillMaxSize()) {
//                FeedTabsContent(pagerState, navController)
//            }
        }
    }
}

/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedTabsContent(pagerState: PagerState, navController: NavController) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {

            0 -> {
                FaqScreen(viewModel = hiltViewModel(), navController = navController)
            }
        }

    }
}*/
