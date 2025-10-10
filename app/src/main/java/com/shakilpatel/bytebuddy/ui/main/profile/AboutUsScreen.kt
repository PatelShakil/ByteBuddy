package com.shakilpatel.bytebuddy.ui.main.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.uicomponents.LinkifyText
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.admin.ContactsModel
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AboutUsScreen(viewModel: ProfileViewModel) {
    ByteBuddyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            // Animated Background
            AnimatedAboutBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sp(40.dp)

                // Profile Header Section
                AnimatedProfileHeader(viewModel)

                Sp(32.dp)

                // Developer Info Card
                DeveloperInfoCard(viewModel)

                Sp(24.dp)

                // Skills & Expertise Section
                SkillsSection()

                Sp(24.dp)

                // Social Media Contacts
                SocialMediaSection()

                Sp(32.dp)

                // Footer
                AboutFooter()

                Sp(32.dp)
            }
        }
    }
}

@Composable
fun AnimatedAboutBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "about_bg")

    val circle1Y by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1"
    )

    val circle2Y by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Floating circle 1
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-50).dp, y = circle1Y.dp + 100.dp)
                .blur(60.dp)
                .background(MainColor.copy(alpha = 0.15f), CircleShape)
        )

        // Floating circle 2
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = 250.dp, y = circle2Y.dp + 400.dp)
                .blur(50.dp)
                .background(Color(0xFF8B5CF6).copy(alpha = 0.12f), CircleShape)
        )
    }
}

@Composable
fun AnimatedProfileHeader(viewModel: ProfileViewModel) {
    var isVisible by remember { mutableStateOf(false) }
    var devProfile by remember { mutableStateOf("") }

    // Fetch developer profile
    LaunchedEffect(Unit) {
        viewModel.database.reference.child("devProfile")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        devProfile = snapshot.value.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        delay(200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(160.dp)
        ) {
            // Animated Ring
            AnimatedProfileRing()

            // Profile Image
            if (devProfile.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_round_img),
                    contentDescription = "Developer Profile",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(WhiteColor, CircleShape)
                        .border(4.dp, MainColor.copy(alpha = 0.3f), CircleShape)
                )
            } else {
                AsyncImage(
                    model = devProfile,
                    contentDescription = "Developer Profile",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(WhiteColor, CircleShape)
                        .border(4.dp, MainColor.copy(alpha = 0.3f), CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_profile_round_img)
                )
            }

            // Verified Badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = (-8).dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(MainColor, MainColor.copy(alpha = 0.8f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BoxScope.AnimatedProfileRing() {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(
        modifier = Modifier
            .size(160.dp)
            .align(Alignment.Center)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2

        repeat(3) { index ->
            val angle = (rotation + (index * 120f)) * PI / 180
            val x = center.x + radius * 0.9f * cos(angle).toFloat()
            val y = center.y + radius * 0.9f * sin(angle).toFloat()

            drawCircle(
                color = MainColor.copy(alpha = 0.3f),
                radius = 6f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun DeveloperInfoCard(viewModel: ProfileViewModel) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(800)
                )
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
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Name
                Text(
                    text = "MuhammadShakil Patel",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Sp(8.dp)

                // Role Badge
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MainColor.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = MainColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Full-Stack Developer & Creator",
                            color = MainColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                Sp(20.dp)

                // Description
                LinkifyText(
                    text = viewModel.aboutMsg.value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Sp(20.dp)

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Default.Code,
                        label = "Tech Savvy\nSolution",
                        value = "CEO"
                    )
                    StatItem(
                        icon = Icons.Default.WorkOutline,
                        label = "Projects",
                        value = "40+"
                    )
                    StatItem(
                        icon = Icons.Default.FavoriteBorder,
                        label = "Made with",
                        value = "❤️"
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, value: String) {
    Column(
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
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF0F172A)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
fun SkillsSection() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(600)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(initialOffsetY = { 50 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MainColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tech Stack & Expertise",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 20.sp
                )
            }

            Sp(16.dp)

            val skills = listOf(
                "Android • Jetpack Compose" to Icons.Default.Android,
                "Kotlin • Java • Python" to Icons.Default.Code,
                "Flutter • React • Web Dev" to Icons.Default.Web,
                "Firebase • Cloud Solutions" to Icons.Default.Cloud
            )

            skills.forEachIndexed { index, (skill, icon) ->
                SkillChip(skill = skill, icon = icon, index = index)
                Sp(12.dp)
            }
        }
    }
}

@Composable
fun SkillChip(skill: String, icon: ImageVector, index: Int) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        delay(index * 100L)
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
            .fillMaxWidth()
            .scale(scale.value),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MainColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MainColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = skill,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B),
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SocialMediaSection() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(initialOffsetY = { 50 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = MainColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Connect With Me",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 20.sp
                )
            }

            Sp(16.dp)

            val contacts = listOf(
                ContactsModel(
                    "https://play-lh.googleusercontent.com/bYtqbOcTYOlgc6gqZ2rwb8lptHuwlNE75zYJu6Bn076-hTmvd96HH-6v7S0YUAAJXoJN=w240-h480-rw",
                    "WhatsApp",
                    "https://wa.me/919510634082"
                ),
                ContactsModel(
                    "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                    "GitHub",
                    "https://github.com/PatelShakil"
                ),
                ContactsModel(
                    "https://pbs.twimg.com/profile_images/1661161645857710081/6WtDIesg_400x400.png",
                    "LinkedIn",
                    "https://www.linkedin.com/in/muhammadshakil-mustak-75775623a"
                ),
                ContactsModel(
                    "https://play-lh.googleusercontent.com/ZU9cSsyIJZo6Oy7HTHiEPwZg0m2Crep-d5ZrfajqtsH-qgUXSqKpNA2FpPDTn-7qA5Q=w240-h480-rw",
                    "Telegram",
                    "https://t.me/patelshakil95"
                ),
                ContactsModel(
                    "https://play-lh.googleusercontent.com/VRMWkE5p3CkWhJs6nv-9ZsLAs1QOg5ob1_3qg-rckwYW7yp1fMrYZqnEFpk0IoVP4LM=s48-rw",
                    "Instagram",
                    "https://instagram.com/patelshakil95"
                ),
                ContactsModel(
                    "https://tech-savvy-solution.web.app/assets/logo_home.png",
                    "Website",
                    "https://tech-savvy-solution.web.app"
                )
            )

            contacts.chunked(2).forEachIndexed { rowIndex, rowContacts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowContacts.forEach { contact ->
                        ModernSocialCard(
                            contact = contact,
                            modifier = Modifier.weight(1f),
                            index = rowIndex * 2 + rowContacts.indexOf(contact)
                        )
                    }
                    // Fill remaining space if odd number
                    if (rowContacts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Sp(12.dp)
            }
        }
    }
}

@Composable
fun ModernSocialCard(
    contact: ContactsModel,
    modifier: Modifier = Modifier,
    index: Int
) {
    val context = LocalContext.current
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        delay(index * 80L)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Card(
        modifier = modifier
            .scale(scale.value)
            .clickable { Cons.gotoUrl(contact.url, context) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = contact.icon,
                contentDescription = contact.platformName,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = contact.platformName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AboutFooter() {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                slideInVertically(initialOffsetY = { 40 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MainColor.copy(alpha = 0.05f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Made with ❤️ in India",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        fontSize = 14.sp
                    )
                    Sp(8.dp)
                    Text(
                        text = "Version ${Cons.getCurrentVersionName(context)}",
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
