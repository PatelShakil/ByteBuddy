package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.shakilpatel.notesapp.common.Cons
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ZoomableImage(image: String) {

    val zoomState = rememberZoomState()
    Image(
        bitmap = Cons.decodeImage(image).asImageBitmap(),
        contentDescription = "Zoomable image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .zoomable(zoomState),
    )
//    val scale = remember { mutableStateOf(1f) }
//    val rotationState = remember { mutableStateOf(1f) }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RectangleShape) // Clip the box content
//            .height(500.dp) // Give the size you want...
//            .background(Color.Transparent)
//            .pointerInput(Unit) {
//                detectTransformGestures { centroid, pan, zoom, rotation ->
//                    scale.value *= zoom
//                    rotationState.value += rotation
//                }
//            }
//    ) {
//        Image(
//            modifier = Modifier
//                .align(Alignment.Center) // keep the image centralized into the Box
//                .graphicsLayer(
//                    // adding some zoom limits (min 50%, max 200%)
//                    scaleX = maxOf(.5f, minOf(3f, scale.value)),
//                    rotationZ = rotationState.value
//                ),
//            contentDescription = null,
//            bitmap = Cons.decodeImage(image).asImageBitmap()
//        )
//    }
}