package com.shakilpatel.notesapp.common.tools.pdflib

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
internal fun PdfImage(
    bitmap: () -> ImageBitmap,
    contentDescription: String = "",
) {
    Image(
        bitmap = bitmap(),
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
//            .zoomable(rememberZoomState())
    )

}
