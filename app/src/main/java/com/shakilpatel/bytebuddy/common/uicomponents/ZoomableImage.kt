package com.shakilpatel.bytebuddy.common.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.shakilpatel.bytebuddy.common.Cons
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