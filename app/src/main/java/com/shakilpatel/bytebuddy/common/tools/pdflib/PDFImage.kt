package com.shakilpatel.bytebuddy.common.tools.pdflib

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
internal fun PdfImage(
    bitmap: () -> ImageBitmap,
    contentDescription: String = "",
) {
    Image(
        bitmap = bitmap(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize(),
//            .zoomable(rememberZoomState())
    )

}
