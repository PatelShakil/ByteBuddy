package com.shakilpatel.bytebuddy.ui.splash

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.uicomponents.PoweredByTechSavvy
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(viewModel: SplashViewModel, navController: NavController) {
    HideSystemBars()
    ByteBuddyTheme {
        val context = LocalContext.current

        LaunchedEffect(key1 = true) {
            delay(3000L) // Give time for animations to complete
//            return@LaunchedEffect
            if (Cons.isInternetConnected(context)) {
                viewModel.isUserExists() { userExists ->
                    if (userExists) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Auth.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Internet Connection not available", Toast.LENGTH_SHORT).show()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEFEFE))
        ) {
            // Animated Gradient Background
            AnimatedSplashBackground()

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                // Animated Logo with Icons
                AnimatedLogoSection()

                Spacer(modifier = Modifier.height(40.dp))

                // App Name with Animation
                AnimatedAppName()

                Spacer(modifier = Modifier.height(16.dp))

                // Tagline
                AnimatedTagline()

                Spacer(modifier = Modifier.weight(1f))

                // Loading Indicator
                AnimatedLoadingIndicator()

                Spacer(modifier = Modifier.height(24.dp))

                // Powered By Footer
                AnimatedFooter()
            }
        }
    }
}

@Composable
fun AnimatedSplashBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_bg")

    // Animated gradient colors
    val color1 by infiniteTransition.animateColor(
        initialValue = MainColor.copy(alpha = 0.1f),
        targetValue = MainColor.copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF6366F1).copy(alpha = 0.1f),
        targetValue = Color(0xFF8B5CF6).copy(alpha = 0.25f),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color2"
    )

    // Rotating gradient
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
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
                        Color.White,
                        color1,
                        color2,
                        Color.White
                    ),
                    start = Offset(
                        x = 800f * cos(angle * PI / 180).toFloat(),
                        y = 800f * sin(angle * PI / 180).toFloat()
                    ),
                    end = Offset(
                        x = 800f * cos((angle + 180) * PI / 180).toFloat(),
                        y = 800f * sin((angle + 180) * PI / 180).toFloat()
                    )
                )
            )
    ) {
        // Floating circles
        FloatingSplashCircles()
    }
}

@Composable
fun BoxScope.FloatingSplashCircles() {
    val infiniteTransition = rememberInfiniteTransition(label = "circles")

    val circle1Y by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1Y"
    )

    val circle2Y by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = -50f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2Y"
    )

    // Circle 1
    Box(
        modifier = Modifier
            .size(250.dp)
            .offset(x = (-80).dp, y = circle1Y.dp + 100.dp)
            .blur(60.dp)
            .background(MainColor.copy(alpha = 0.2f), CircleShape)
            .align(Alignment.TopStart)
    )

    // Circle 2
    Box(
        modifier = Modifier
            .size(200.dp)
            .offset(x = 30.dp, y = circle2Y.dp - 100.dp)
            .blur(50.dp)
            .background(Color(0xFF8B5CF6).copy(alpha = 0.15f), CircleShape)
            .align(Alignment.BottomEnd)
    )
}

@Composable
fun AnimatedLogoSection() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Orbiting Icons
        OrbitingIcons(isVisible = isVisible)

        // Central Logo
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(animationSpec = tween(800))
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(MainColor, MainColor.copy(alpha = 0.8f))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "ByteBuddy Logo",
//                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun OrbitingIcons(isVisible: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbit")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val icons = listOf(
        Icons.Default.MenuBook to Color(0xFF6366F1),
        Icons.Default.QuestionAnswer to Color(0xFF8B5CF6),
        Icons.Default.Group to Color(0xFF3B82F6),
        Icons.Default.Upload to Color(0xFF10B981)
    )

    icons.forEachIndexed { index, (icon, color) ->
        val angle = (rotation + (index * 90f)) * PI / 180
        val radius = 100f

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                initialScale = 0f
            ) + fadeIn(
                animationSpec = tween(600, delayMillis = 200 + (index * 100))
            )
        ) {
            Box(
                modifier = Modifier
                    .offset(
                        x = (radius * cos(angle)).dp,
                        y = (radius * sin(angle)).dp
                    )
                    .size(48.dp)
                    .background(color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedAppName() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(1000)) +
                slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
    ) {
        Text(
            text = "ByteBuddy",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 48.sp,
            color = Color(0xFF1E293B),
            letterSpacing = (-1).sp
        )
    }
}

@Composable
fun AnimatedTagline() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(800)
                )
    ) {
        Text(
            text = "The Ultimate EduCollab Platform",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun AnimatedLoadingIndicator() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1600)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + scaleIn(animationSpec = tween(600))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                LoadingDot(index = index)
            }
        }
    }
}

@Composable
fun LoadingDot(index: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot_$index")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = index * 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = index * 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(12.dp)
            .scale(scale)
            .alpha(alpha)
            .background(MainColor, CircleShape)
    )
}

@Composable
fun AnimatedFooter() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(800)
                )
    ) {
        PoweredByTechSavvy()
    }
}

internal tailrec fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
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
