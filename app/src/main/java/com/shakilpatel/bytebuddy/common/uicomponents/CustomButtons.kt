package com.shakilpatel.bytebuddy.common.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shakilpatel.bytebuddy.common.MainColor

@Composable
fun CustomOutlinedButton(isEnable : Boolean = true,modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, MainColor),
        modifier = modifier,
        enabled = isEnable ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = label,
            color = MainColor
        )
    }
}

@Composable
fun CustomButtonBottomRightIcon(modifier: Modifier, label: String,onClick: () -> Unit) {
    Box(modifier = modifier.clickable {
        onClick()
    }
        ) {
        Box(modifier = modifier.padding(bottom = 5.dp, end = 5.dp))
            Card(border = BorderStroke(.5.dp, MainColor),
            modifier = Modifier.padding(10.dp),
                colors = CardDefaults.cardColors(
                    Color.Transparent
                )) {
                Text(
                    label,
                    modifier = modifier
                        .align(CenterHorizontally)
                        .padding(15.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        Box(    modifier = Modifier
            .clip(CircleShape)
            .padding(top = 15.dp)
            .size(30.dp)
            .align(Alignment.BottomEnd)
            .background(MainColor, CircleShape),
            contentAlignment = Center
        ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "",
            tint = Color.White,
        )
    }

    }

}