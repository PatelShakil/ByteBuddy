package com.shakilpatel.bytebuddy.ui.main.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.uicomponents.PoweredByTechSavvy
import com.shakilpatel.bytebuddy.common.uicomponents.PoweredByTechSavvyCompact
import com.shakilpatel.bytebuddy.common.uicomponents.PoweredByTechSavvyStatic
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.SnackBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.learning.CourseModel
import com.shakilpatel.bytebuddy.data.models.learning.SemModel
import com.shakilpatel.bytebuddy.ui.ads.AdmobBanner
import com.shakilpatel.bytebuddy.ui.main.home.notes.NotesViewModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    notesViewModel: NotesViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    ByteBuddyTheme {
        BackHandler { onBack() }

        val coursesList = viewModel.courses.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            // Animated Background
            AnimatedGradientBackground()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Hero Banner Section
                item(key = "banner") {
                    AnimatedBannerSection(viewModel = viewModel)
                }


                // Courses Section
                coursesList.value.let { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            items(
                                items = resource.result,
                                key = { it.name }
                            ) { course ->
                                ModernCourseCard(
                                    course = course,
                                    notesViewModel = notesViewModel,
                                    navController = navController
                                )
                                Sp(20.dp)
                            }
                        }

                        is Resource.Loading -> {
                            item(key = "loading") {
                                ShimmerLoadingEffect()
                            }
                        }

                        is Resource.Failure -> {
                            item(key = "error") {
                                ErrorState(message = resource.errorMsgBody)
                            }
                        }

                        else -> {
                            item(key = "progress") {
                                ProgressBarIndicator()
                            }
                        }
                    }
                }
                // Welcome Header with Stats
                item(key = "welcome") {
                    WelcomeHeaderWithStats()
                }


                // Ad Banner
                item(key = "admob") {
                    Sp(20.dp)
                    AdmobBanner(modifier = Modifier.fillMaxWidth())
                }

                // Footer
                item(key = "footer") {
                    Sp(20.dp)
                    ContactUsCard {
                        navController.navigate(Screen.Main.Profile.AboutUs.route)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    // Animate gradient colors
    val color1 by infiniteTransition.animateColor(
        initialValue = MainColor.copy(alpha = 0.3f),
        targetValue = MainColor.copy(alpha = 0.6f),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF6366F1).copy(alpha = 0.2f),
        targetValue = Color(0xFF8B5CF6).copy(alpha = 0.4f),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color2"
    )

    // Animate gradient angle
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        color1,
                        color2,
                        Color(0xFF3B82F6).copy(alpha = 0.3f),
                        color1
                    ),
                    start = Offset(
                        x = 500f * cos(angle * PI / 180).toFloat(),
                        y = 500f * sin(angle * PI / 180).toFloat()
                    ),
                    end = Offset(
                        x = 500f * cos((angle + 180) * PI / 180).toFloat(),
                        y = 500f * sin((angle + 180) * PI / 180).toFloat()
                    )
                )
            )
    ) {
        // Floating animated circles
//        FloatingCircles()
    }
}

@Composable
fun BoxScope.FloatingCircles() {
    val infiniteTransition = rememberInfiniteTransition(label = "circles")

    val circle1Y by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1"
    )

    val circle2Y by infiniteTransition.animateFloat(
        initialValue = 100f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2"
    )

    // Circle 1
    Box(
        modifier = Modifier
            .size(300.dp)
            .offset(x = (-100).dp, y = circle1Y.dp)
            .blur(80.dp)
            .background(
                MainColor.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .align(Alignment.TopStart)
    )

    // Circle 2
    Box(
        modifier = Modifier
            .size(250.dp)
            .offset(x = 50.dp, y = circle2Y.dp)
            .blur(70.dp)
            .background(
                Color(0xFF8B5CF6).copy(alpha = 0.2f),
                shape = CircleShape
            )
            .align(Alignment.BottomEnd)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AnimatedBannerSection(viewModel: HomeViewModel) {
    val banners = viewModel.banners.collectAsState()

    when (val resource = banners.value) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        is Resource.Success -> {
            if (resource.result.isNotEmpty()) {
                val uriHandler = LocalUriHandler.current
                val pagerState = rememberPagerState(initialPage = 0)

                LaunchedEffect(Unit) {
                    while (true) {
                        yield()
                        delay(4000)
                        pagerState.animateScrollToPage(
                            page = (pagerState.currentPage + 1) % pagerState.pageCount,
                            animationSpec = tween(
                                durationMillis = 800,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager(
                        count = resource.result.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val item = resource.result.sortedBy { it.position }[page]

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(horizontal = 20.dp),
                            elevation = CardDefaults.cardElevation(12.dp),
                            shape = RoundedCornerShape(28.dp),
                            onClick = { uriHandler.openUri(item.url) }
                        ) {
                            Box {
                                AsyncImage(
                                    model = item.image,
                                    contentDescription = "Banner",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                // Gradient overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.3f)
                                                )
                                            )
                                        )
                                )
                            }
                        }
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier.padding(12.dp),
                        activeColor = MainColor,
                        inactiveColor = Color.White.copy(alpha = 0.4f),
                        indicatorWidth = 10.dp,
                        indicatorHeight = 10.dp,
                        spacing = 8.dp
                    )
                }
            }
        }

        is Resource.Failure -> {}
        else -> {}
    }
}

@Composable
fun WelcomeHeaderWithStats() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(initialOffsetY = { -40 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Welcome Text
//            Text(
//                text = "Welcome Back! 👋",
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.ExtraBold,
//                color = Color.White,
//                fontSize = 32.sp
//            )
//            Sp(8.dp)
//            Text(
//                text = "Let's continue your learning journey",
//                style = MaterialTheme.typography.bodyLarge,
//                color = Color.White.copy(alpha = 0.9f),
//                fontSize = 16.sp
//            )
//
//            Sp(20.dp)

            // Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    icon = Icons.Default.School,
                    value = "300+",
                    label = "Students",
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    icon = Icons.Default.MenuBook,
                    value = "200+",
                    label = "Notes",
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    icon = Icons.Default.Group,
                    value = "Active",
                    label = "Community",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatsCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Card(
        modifier = modifier.scale(scale.value),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(28.dp)
            )
            Sp(8.dp)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MainColor,
                fontSize = 20.sp
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MainColor.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ModernCourseCard(
    course: CourseModel,
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) +
                scaleIn(initialScale = 0.9f, animationSpec = tween(600))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFF8FAFC)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                // Course Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Animated Icon
                    val rotation = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        rotation.animateTo(
                            targetValue = 360f,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .rotate(rotation.value)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MainColor,
                                        MainColor.copy(alpha = 0.7f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = course.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 22.sp
                        )
                        if (course.universityName.isNotEmpty()) {
                            Sp(4.dp)
                            Text(
                                text = course.universityName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Badge
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MainColor.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = "${course.semList.size} Sem",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MainColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }

                if (course.description.isNotEmpty()) {
                    Sp(16.dp)
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Sp(20.dp)

                // Semesters Grid
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(
                        items = course.semList,
                        key = { it.id }
                    ) { sem ->
                        SemesterCardModern(
                            sem = sem,
                            onSemClick = {
                                notesViewModel.setCourse(it.courseId + it.name)
                                notesViewModel.setSubject(it.id)

                                if ((it.courseId + it.name).contains("BCA") ||
                                    (it.courseId + it.name).contains("MSC ICT")
                                ) {
                                    navController.navigate(
                                        Screen.Main.Home.Subject.route + "/${it.courseId + it.name}"
                                    )
                                } else {
                                    navController.navigate(
                                        Screen.Main.Home.Notes.route + "/${it.id}/${it.courseId}"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SemesterCardModern(
    sem: SemModel,
    onSemClick: (SemModel) -> Unit
) {
    val scale = remember { Animatable(0.85f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .scale(scale.value),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MainColor
        ),
        onClick = { onSemClick(sem) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MainColor,
                            MainColor.copy(alpha = 0.8f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Sp(12.dp)
                Text(
                    text = sem.id,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
//                if (sem.name.isNotEmpty()) {
//                    Sp(4.dp)
//                    Text(
//                        text = sem.name,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.White.copy(alpha = 0.95f),
//                        textAlign = TextAlign.Center,
//                        fontSize = 11.sp,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier.padding(horizontal = 8.dp)
//                    )
//                }
            }
        }
    }
}

@Composable
fun ContactUsCard(onClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) +
                slideInVertically(initialOffsetY = { 40 })
    ) {
        PoweredByTechSavvy(
            modifier = Modifier.clickable(
                onClick = onClick
            )
        )
    }
}

@Composable
fun ShimmerLoadingEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = alpha * 0.3f))
            )
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(64.dp)
            )
            Sp(16.dp)
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Sp(8.dp)
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
