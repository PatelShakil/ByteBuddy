package com.shakilpatel.notesapp.ui.splash

import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.getRandomLightColor
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel,navController: NavController) {
    ByteBuddyTheme {
        val scale = remember{
            Animatable(0f)
        }
        val context = LocalContext.current
        LaunchedEffect(key1 = true, block = {
            scale.animateTo(1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(1500L)
            if(Cons.isInternetConnected(context)){
                viewModel.isUserExists(navController)
            }else{
                Toast.makeText(context, "Internet Connection not available", Toast.LENGTH_SHORT)
                    .show()
            }

        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
//                    brush = Brush.linearGradient(
//                        0.35f to androidx.compose.material3.MaterialTheme.colorScheme.primary,
//                        0.65f to MaterialTheme.colorScheme.secondary,
//                    )
                    HorizontalBrush
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale.value),
            )
        }
    }
}