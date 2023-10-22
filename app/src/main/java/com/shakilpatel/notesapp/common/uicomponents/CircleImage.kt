package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.MainColor

@Composable
fun CircularImage(size: Dp,image: String?,onClick:()->Unit) {
    Box(modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .clickable {
            onClick()
        },
    contentAlignment = Alignment.Center){
        if(image == null || image == ""){
            Icon(
                painterResource(id = R.drawable.ic_image), "",
                tint = MainColor,
                modifier = Modifier
                    .size(size)
                    .border(.5.dp, MainColor, CircleShape)
                    .padding(10.dp)
            )

        }else {
            Image(
                Cons.decodeImage(image).asImageBitmap(), "",
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
fun CircularImage(image: String) {
    Box(modifier = Modifier
        .size(55.dp)
        .clip(CircleShape),
    contentAlignment = Alignment.Center){
        if(image == null){
            Icon(
                painterResource(id = R.drawable.ic_image), "",
                tint = MainColor,
                modifier = Modifier
                    .size(55.dp)
                    .border(.5.dp, MainColor, CircleShape)
                    .padding(10.dp)
            )

        }else {
            AsyncImage(
                model = image, "",
                contentScale = ContentScale.Crop
            )
        }
    }
}