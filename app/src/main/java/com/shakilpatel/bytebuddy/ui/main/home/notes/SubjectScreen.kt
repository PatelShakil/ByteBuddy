package com.shakilpatel.bytebuddy.ui.main.home.notes

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.SubjectAppBar
import com.shakilpatel.bytebuddy.data.models.learning.SubjectModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SubjectScreen(course: String, viewModel: NotesViewModel, navController: NavController) {
    val sem = course.replace("BCA", "").replace("MSC ICT", "")
    viewModel.courses.collectAsState().value.let { resource ->
        when (resource) {
            is Resource.Success -> {
                ByteBuddyTheme {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF8FAFC))
                    ) {
                        // Animated Background
                        AnimatedSubjectBackground()

                        Column(modifier = Modifier.fillMaxSize()) {
                            SubjectAppBar(sem, navController)

                            val subjects = if (sem.isNotEmpty()) {
                                Log.d("Course", "SubjectScreen: $course")
                                resource.result
                                    .find { course.contains(it.name) }
                                    ?.semList
                                    ?.filter { it.name == sem }
                                    ?.get(0)
                                    ?.subjects
                                    ?.sortedBy { it.id.substring(0, 3) }
                                    ?: emptyList()
                            } else {
                                emptyList()
                            }

                            if (subjects.isEmpty()) {
                                EmptySubjectStateView()
                            } else {
                                SubjectListContent(
                                    subjects = subjects,
                                    course = course,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun AnimatedSubjectBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "subject_bg")

    val circle1Y by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1"
    )

    val circle2Y by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-40).dp, y = circle1Y.dp + 100.dp)
                .blur(50.dp)
                .background(MainColor.copy(alpha = 0.12f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = 280.dp, y = circle2Y.dp + 300.dp)
                .blur(45.dp)
                .background(Color(0xFF8B5CF6).copy(alpha = 0.1f), CircleShape)
        )
    }
}

@Composable
fun SubjectListContent(
    subjects: List<SubjectModel>,
    course: String,
    viewModel: NotesViewModel,
    navController: NavController
) {
    var isHeaderVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isHeaderVisible = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        // Header Section
        item(key = "header") {
            AnimatedVisibility(
                visible = isHeaderVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(initialOffsetY = { -40 })
            ) {
                SubjectListHeader(totalSubjects = subjects.size)
            }
            Sp(24.dp)
        }

        // Subject Items
        itemsIndexed(
            items = subjects,
            key = { _, subject -> subject.id }
        ) { index, subject ->
            AnimatedSubjectItem(
                subject = subject,
                index = index,
                onClick = {
                    viewModel.setSubject(subject.id)
                    viewModel.getNotesCol(subject.subjectName, course)
                    navController.navigate(
                        Screen.Main.Home.Notes.route + "/${subject.subjectName}/${course}"
                    )
                }
            )
            Sp(16.dp)
        }
    }
}

@Composable
fun SubjectListHeader(totalSubjects: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MainColor.copy(alpha = 0.1f),
                            Color(0xFF8B5CF6).copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MainColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Subject,
                        contentDescription = null,
                        tint = MainColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Your Subjects",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "$totalSubjects subjects available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedSubjectItem(
    subject: SubjectModel,
    index: Int,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 80L + 400L)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInHorizontally(
                    initialOffsetX = { 100 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
    ) {
        ModernSubjectCard(
            subject = subject,
            index = index,
            onClick = onClick
        )
    }
}

@Composable
fun ModernSubjectCard(
    subject: SubjectModel,
    index: Int,
    onClick: () -> Unit
) {
    val subjectIcon = getSubjectIcon(subject.subjectName)
    val subjectColor = getSubjectColor(index)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            subjectColor.copy(alpha = 0.03f)
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Icon Container
            AnimatedSubjectIcon(
                icon = subjectIcon,
                color = subjectColor,
                subjectId = subject.id
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Subject Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = subject.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Sp(4.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subject.id,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (subject.notesList.isNotEmpty()) {
                        Badge(
                            containerColor = subjectColor.copy(alpha = 0.15f),
                            contentColor = subjectColor
                        ) {
                            Text(
                                text = "${subject.notesList.size} notes",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Arrow Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(subjectColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = subjectColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedSubjectIcon(
    icon: ImageVector,
    color: Color,
    subjectId: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon_$subjectId")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        color,
                        color.copy(alpha = 0.8f)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun EmptySubjectStateView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(80.dp)
            )
            Sp(24.dp)
            Text(
                text = "No Subjects Found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Sp(8.dp)
            Text(
                text = "Subjects will appear here once added",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

// Helper function to get subject-specific icons
fun getSubjectIcon(subjectName: String): ImageVector {
    return when {
        subjectName.contains("Math", ignoreCase = true) ||
                subjectName.contains("Calculus", ignoreCase = true) -> Icons.Default.Calculate

        subjectName.contains("Program", ignoreCase = true) ||
                subjectName.contains("Code", ignoreCase = true) ||
                subjectName.contains("Java", ignoreCase = true) ||
                subjectName.contains("Python", ignoreCase = true) -> Icons.Default.Code

        subjectName.contains("Data", ignoreCase = true) ||
                subjectName.contains("Database", ignoreCase = true) ||
                subjectName.contains("DBMS", ignoreCase = true) -> Icons.Default.Storage

        subjectName.contains("Network", ignoreCase = true) ||
                subjectName.contains("Internet", ignoreCase = true) -> Icons.Default.Language

        subjectName.contains("Web", ignoreCase = true) ||
                subjectName.contains("HTML", ignoreCase = true) -> Icons.Default.Web

        subjectName.contains("Algorithm", ignoreCase = true) ||
                subjectName.contains("DSA", ignoreCase = true) -> Icons.Default.AccountTree

        subjectName.contains("System", ignoreCase = true) ||
                subjectName.contains("OS", ignoreCase = true) ||
                subjectName.contains("Operating", ignoreCase = true) -> Icons.Default.Computer

        subjectName.contains("Software", ignoreCase = true) ||
                subjectName.contains("Engineer", ignoreCase = true) -> Icons.Default.Engineering

        subjectName.contains("Security", ignoreCase = true) ||
                subjectName.contains("Cyber", ignoreCase = true) -> Icons.Default.Security

        subjectName.contains("Design", ignoreCase = true) ||
                subjectName.contains("UI", ignoreCase = true) -> Icons.Default.DesignServices

        subjectName.contains("Graphics", ignoreCase = true) ||
                subjectName.contains("Multimedia", ignoreCase = true) -> Icons.Default.Image

        subjectName.contains("AI", ignoreCase = true) ||
                subjectName.contains("Intelligence", ignoreCase = true) ||
                subjectName.contains("ML", ignoreCase = true) -> Icons.Default.Psychology

        subjectName.contains("Cloud", ignoreCase = true) -> Icons.Default.Cloud

        subjectName.contains("Mobile", ignoreCase = true) ||
                subjectName.contains("Android", ignoreCase = true) -> Icons.Default.PhoneAndroid

        subjectName.contains("Theory", ignoreCase = true) ||
                subjectName.contains("Concept", ignoreCase = true) -> Icons.Default.LibraryBooks

        else -> Icons.Default.MenuBook
    }
}

// Helper function to get color based on index
fun getSubjectColor(index: Int): Color {
    val colors = listOf(
        Color(0xFF6366F1), // Indigo
        Color(0xFF8B5CF6), // Purple
        Color(0xFF06B6D4), // Cyan
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Amber
        Color(0xFFEF4444), // Red
        Color(0xFF3B82F6), // Blue
        Color(0xFFEC4899), // Pink
        Color(0xFF14B8A6), // Teal
        Color(0xFFF97316)  // Orange
    )
    return colors[index % colors.size]
}
