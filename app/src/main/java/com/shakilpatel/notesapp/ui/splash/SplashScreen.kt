package com.shakilpatel.notesapp.ui.splash

import android.content.Context
import android.content.ContextWrapper
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.ui.nav.Screen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel, navController: NavController) {
    HideSystemBars()
    ByteBuddyTheme {
        val context = LocalContext.current
        LaunchedEffect(key1 = true, block = {
            delay(2600L)
            if (Cons.isInternetConnected(context)) {
                viewModel.isUserExists() {
                    if (it) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(Screen.Auth.Login.route) {
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Internet Connection not available", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFFEFEFE)
                )
        ) {
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()

            Image(
                rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.logo_anim).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(bottom = 130.dp)
            )
            Image(
                painterResource(R.drawable.powered_by),
                "",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 70.dp),
                contentScale = ContentScale.Crop
            )
        }
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