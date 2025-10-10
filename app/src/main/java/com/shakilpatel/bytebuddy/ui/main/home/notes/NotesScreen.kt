package com.shakilpatel.bytebuddy.ui.main.home.notes

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.uicomponents.NotesAppBar
import com.shakilpatel.bytebuddy.common.uicomponents.OnNoDataFound
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.SearchBar
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.ui.ads.AdmobBanner
import com.shakilpatel.bytebuddy.ui.ads.showInterstialAd
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NotesScreen(
    subjectName: String,
    courseName: String,
    viewModel: NotesViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    ByteBuddyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            // Animated Background
            AnimatedNotesBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                NotesAppBar(title = subjectName, navController) {}

                // Modern Tab Bar
                val pagerState = rememberPagerState()
                val tabList = listOf("All", "Official", "Unofficial")
                ModernTabBar(
                    tabs = tabList,
                    selectedTab = pagerState.currentPage,
                    onTabSelected = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )

                // Content
                viewModel.notesCol.collectAsState().value.let { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            HorizontalPager(
                                count = tabList.size,
                                state = pagerState
                            ) { page ->
                                val filteredNotes = when (page) {
                                    0 -> resource.result
                                    1 -> resource.result.filter { it.verified }
                                    2 -> resource.result.filter { !it.verified }
                                    else -> emptyList()
                                }
                                NotesList(
                                    list = filteredNotes,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }
                        }
                        is Resource.Failure -> {
                            ErrorStateView(message = resource.errorMsgBody)
                        }
                        is Resource.Loading -> {
                            LoadingStateView()
                        }
                        else -> {
                            EmptyStateView()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedNotesBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "notes_bg")

    val circle1Y by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1"
    )

    val circle2Y by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-30).dp, y = circle1Y.dp + 150.dp)
                .blur(45.dp)
                .background(MainColor.copy(alpha = 0.1f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(x = 260.dp, y = circle2Y.dp + 400.dp)
                .blur(40.dp)
                .background(Color(0xFF8B5CF6).copy(alpha = 0.08f), CircleShape)
        )
    }
}
@Composable
fun ModernTabBar(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400)) +
                slideInVertically(initialOffsetY = { -20 })
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp), // Reduced padding
            shape = RoundedCornerShape(16.dp), // Slightly smaller corners
            elevation = CardDefaults.cardElevation(3.dp), // Reduced elevation
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), // Reduced inner padding
                horizontalArrangement = Arrangement.spacedBy(6.dp) // Reduced spacing
            ) {
                tabs.forEachIndexed { index, tab ->
                    TabItem(
                        text = tab,
                        isSelected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MainColor else Color.Transparent,
        animationSpec = tween(300),
        label = "bg_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF64748B),
        animationSpec = tween(300),
        label = "text_color"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)) // Smaller corner radius
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 8.dp), // Reduced vertical padding
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp) // Smaller icon
                )
                Spacer(modifier = Modifier.width(4.dp)) // Reduced spacing
            }
            Text(
                text = text,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp // Smaller font size
            )
        }
    }
}


@Composable
fun NotesList(
    list: List<NotesModel>,
    viewModel: NotesViewModel,
    navController: NavController
) {
    var query by remember { mutableStateOf("") }
    val filteredNotes = remember(query, list) {
        if (query.isNotEmpty()) {
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.subjectId.contains(query, ignoreCase = true) ||
                        it.courseId.contains(query, ignoreCase = true)
            }
        } else {
            list
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        SearchBar(
            hint = "Search notes by title or subject",
            searchStr = query,
            onTextChanged = { query = it }
        )

        if (filteredNotes.isEmpty()) {
            EmptySearchResultView()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = filteredNotes,
                    key = { _, note -> note.id }
                ) { index, note ->
                    AnimatedNoteCard(
                        note = note,
                        index = index,
                        viewModel = viewModel,
                        navController = navController
                    )

                    // Ad insertion every 6 items
                    if ((index + 1) % 6 == 0 && index != 0) {
                        // Ad would go here in actual implementation
                    }
                }

                // Footer Ad
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        AdmobBanner()
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedNoteCard(
    note: NotesModel,
    index: Int,
    viewModel: NotesViewModel,
    navController: NavController
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 50L + 200L)
        isVisible = true
    }

//    AnimatedVisibility(
//        visible = isVisible,
//        enter = fadeIn(animationSpec = tween(400)) +
//                scaleIn(
//                    initialScale = 0.8f,
//                    animationSpec = spring(
//                        dampingRatio = Spring.DampingRatioMediumBouncy,
//                        stiffness = Spring.StiffnessMedium
//                    )
//                )
//    ) {
        ModernNoteCard(
            note = note,
            viewModel = viewModel,
            navController = navController
        )
//    }
}

@Composable
fun ModernNoteCard(
    note: NotesModel,
    viewModel: NotesViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var user by remember { mutableStateOf(UserModel()) }
    var isSaved by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Fetch user data
    LaunchedEffect(note.author) {
        viewModel.getUserModel(note.author) { user = it }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserModel { isSaved = it.saved.notes.contains(note.id) }
    }

    // Loading Dialog
    if (isLoading) {
        AdLoadingDialog(
            onDismiss = { isLoading = false }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MainColor.copy(alpha = 0.07f),
                        Color.White
                    )
                )
            )
            .shadow(20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            isLoading = true
            viewModel.notesId.value = note.id
            viewModel.registerView(note.id)

            showInterstialAd(context as Activity) {
                isLoading = false
                viewModel.getNote(note.id)
                navController.navigate(Screen.ViewNotes.Landing.route + "/${note.id}")
            }
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MainColor.copy(alpha = 0.05f),
                                Color.White
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                // Header with Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MainColor,
                                        MainColor.copy(alpha = 0.8f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Verified Badge
                    if (note.verified) {
                        Badge(
                            containerColor = Color(0xFF10B981).copy(alpha = 0.15f),
                            contentColor = Color(0xFF10B981)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Official",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Sp(16.dp)

                // Title
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )

                Sp(12.dp)

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${note.views.size}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Save Button
                    IconButton(
                        onClick = { viewModel.saveNotes(note.id) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isSaved) "Saved" else "Save",
                            tint = if (isSaved) MainColor else Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdLoadingDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { /* Prevent dismissal */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Ad Icon
                AnimatedAdIcon()

                Sp(24.dp)

                Text(
                    text = "Loading Ad...",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Sp(8.dp)

                Text(
                    text = "Please wait while we prepare content",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )

                Sp(24.dp)

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MainColor,
                    trackColor = MainColor.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun AnimatedAdIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "ad_icon")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2

            repeat(3) { index ->
                val angle = (rotation + (index * 120f)) * PI / 180
                val x = center.x + radius * 0.7f * cos(angle).toFloat()
                val y = center.y + radius * 0.7f * sin(angle).toFloat()

                drawCircle(
                    color = MainColor.copy(alpha = 0.6f),
                    radius = 8f,
                    center = Offset(x, y)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.AdUnits,
            contentDescription = null,
            tint = MainColor,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun LoadingStateView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MainColor)
            Sp(16.dp)
            Text(
                text = "Loading notes...",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ErrorStateView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(64.dp)
            )
            Sp(16.dp)
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Sp(8.dp)
            Text(
                text = message,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptySearchResultView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(80.dp)
            )
            Sp(24.dp)
            Text(
                text = "No Results Found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Sp(8.dp)
            Text(
                text = "Try adjusting your search query",
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(80.dp)
            )
            Sp(24.dp)
            Text(
                text = "No Notes Available",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Sp(8.dp)
            Text(
                text = "Notes will appear here once uploaded",
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}
