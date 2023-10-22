package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.TextColor
import com.shakilpatel.notesapp.common.HorizontalBrush

@Composable
fun TabHeader(tabName: String) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MainColor,
                RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            )
            .height(70.dp),
        backgroundColor = MainColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MainColor,
                    RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
            //            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp, topEnd = 0.dp, topStart = 0.dp),
            //            elevation = CardDefaults.cardElevation(
            //                18.dp
            //            ),
            //            colors = CardDefaults.cardColors(
            //                MainColor
            //            )
        ) {
            Sp(h = 10.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent), contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .background(HorizontalBrush, RoundedCornerShape(4.dp))
                        .width(70.dp)
                        .height(8.dp)
                )
            }
            Sp(h = 10.dp)
            Text(
                text = tabName,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                color = TextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}