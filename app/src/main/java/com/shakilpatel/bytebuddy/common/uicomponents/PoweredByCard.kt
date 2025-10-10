package com.shakilpatel.bytebuddy.common.uicomponents

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.bytebuddy.R

/**
 * Native Compose implementation of "Powered By Tech Savvy Solution" branding
 * Eliminates need for image assets while maintaining high quality at any size
 */
@Composable
fun PoweredByTechSavvy(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    logoSize: Dp = 56.dp
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (animate) {
            kotlinx.coroutines.delay(300)
            isVisible = true
        } else {
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(800)
                )
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Animated Logo
            TechSavvyLogo(
                size = logoSize,
                animate = animate
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text Section
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Powered By",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "TECH SAVVY SOLUTION",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF0F172A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Custom drawn Tech Savvy circular logo with TS letters
 */
@Composable
fun TechSavvyLogo(
    size: Dp = 56.dp,
    animate: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_rotation")

    val rotation by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    Box(
        modifier = Modifier.size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.techsavvy_logo),
            contentDescription = "Company Logo",
        )
/*
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size.minDimension
            val center = Offset(canvasSize / 2, canvasSize / 2)
            val radius = canvasSize / 2

            // Outer black circle
            drawCircle(
                color = Color(0xFF0F172A),
                radius = radius,
                center = center
            )

            // Gradient colored arcs (cyan, purple, pink)
            rotate(degrees = rotation, pivot = center) {
                // Cyan arc (T shape - left)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF06B6D4), // Cyan
                            Color(0xFF0EA5E9)
                        )
                    ),
                    startAngle = 180f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(canvasSize * 0.15f, canvasSize * 0.15f),
                    size = Size(canvasSize * 0.7f, canvasSize * 0.7f),
                    style = Stroke(width = canvasSize * 0.1f)
                )

                // Purple arc (middle)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6), // Purple
                            Color(0xFFA855F7)
                        )
                    ),
                    startAngle = 300f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(canvasSize * 0.15f, canvasSize * 0.15f),
                    size = Size(canvasSize * 0.7f, canvasSize * 0.7f),
                    style = Stroke(width = canvasSize * 0.1f)
                )

                // Pink arc (S shape - right)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFFEC4899), // Pink
                            Color(0xFFF472B6)
                        )
                    ),
                    startAngle = 60f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(canvasSize * 0.15f, canvasSize * 0.15f),
                    size = Size(canvasSize * 0.7f, canvasSize * 0.7f),
                    style = Stroke(width = canvasSize * 0.1f)
                )
            }

            // Inner white circle for contrast
            drawCircle(
                color = Color.White,
                radius = radius * 0.45f,
                center = center
            )

            // Draw "TS" text path (simplified geometric representation)
            val textSize = canvasSize * 0.3f
            val textOffsetX = center.x - textSize * 0.5f
            val textOffsetY = center.y - textSize * 0.3f

            // T letter (left side)
            val tPath = Path().apply {
                // Vertical line
                moveTo(textOffsetX + textSize * 0.15f, textOffsetY)
                lineTo(textOffsetX + textSize * 0.15f, textOffsetY + textSize * 0.8f)
                // Top horizontal line
                moveTo(textOffsetX, textOffsetY)
                lineTo(textOffsetX + textSize * 0.35f, textOffsetY)
            }

            drawPath(
                path = tPath,
                color = Color(0xFF06B6D4), // Cyan
                style = Stroke(width = canvasSize * 0.06f, cap = StrokeCap.Round)
            )

            // S letter (right side - simplified)
            val sPath = Path().apply {
                moveTo(textOffsetX + textSize * 0.75f, textOffsetY + textSize * 0.1f)
                cubicTo(
                    textOffsetX + textSize * 0.55f, textOffsetY,
                    textOffsetX + textSize * 0.45f, textOffsetY + textSize * 0.15f,
                    textOffsetX + textSize * 0.55f, textOffsetY + textSize * 0.35f
                )
                cubicTo(
                    textOffsetX + textSize * 0.75f, textOffsetY + textSize * 0.45f,
                    textOffsetX + textSize * 0.65f, textOffsetY + textSize * 0.65f,
                    textOffsetX + textSize * 0.45f, textOffsetY + textSize * 0.7f
                )
            }

            drawPath(
                path = sPath,
                color = Color(0xFFEC4899), // Pink
                style = Stroke(width = canvasSize * 0.06f, cap = StrokeCap.Round)
            )
        }
*/
    }
}

/**
 * Simplified version without animation for static displays
 */
@Composable
fun PoweredByTechSavvyStatic(
    modifier: Modifier = Modifier,
    logoSize: Dp = 48.dp,
    textSize: Int = 16
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TechSavvyLogo(
            size = logoSize,
            animate = false
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Powered By",
                fontSize = (textSize * 0.75).sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "TECH SAVVY SOLUTION",
                fontSize = textSize.sp,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

/**
 * Compact version for footer/small spaces
 */
@Composable
fun PoweredByTechSavvyCompact(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TechSavvyLogo(size = 32.dp, animate = false)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Powered By",
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "TECH SAVVY SOLUTION",
                fontSize = 12.sp,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
        }
    }
}
