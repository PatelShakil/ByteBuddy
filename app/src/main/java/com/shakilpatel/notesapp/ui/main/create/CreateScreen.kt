package com.shakilpatel.notesapp.ui.main.create

//import com.google.accompanist.pager.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CreateScreen(viewModel: CreateViewModel, navController: NavController, onBack: () -> Unit) {
//    BackHandler {
//        onBack()
//    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        2
    }
    ByteBuddyTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.weight(.8f)) {
                TabsContent(pagerState = pagerState, viewModel, navController)
            }
            Tabs(pagerState = pagerState, modifier = Modifier.weight(.05f))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabIndicator(pagerState: PagerState, tabPositions: List<TabPosition>) {
    val targetIndicatorOffset: Dp
    val indicatorWidth: Dp

    val currentTab = tabPositions[pagerState.currentPage]
    val targetPage = pagerState.targetPage
    val targetTab = targetPage?.let { tabPositions.getOrNull(it) }

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction = (pagerState.currentPageOffsetFraction / max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
        indicatorWidth = lerp(currentTab.width, targetTab.width, fraction)
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
        indicatorWidth = currentTab.width
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = targetIndicatorOffset)
            .width(indicatorWidth)
    ) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, modifier: Modifier) {
    val list = listOf(
        "Faq",
        "Notes"
    )
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
                    androidx.compose.material.Text(
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
fun TabsContent(pagerState: PagerState, viewModel: CreateViewModel, navController: NavController) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {

            0 -> {
                FAQTabContent(viewModel = viewModel, navController = navController)
            }

            1 -> {
                NotesTabContent(viewModel = viewModel, navController = navController)
            }
        }
    }
}