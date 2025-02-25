package com.shakilpatel.bytebuddy.common.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shakilpatel.bytebuddy.common.MainColor

@Composable
fun ProgressBarCus(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(shape = RoundedCornerShape(20.dp)) {
            CircularProgressIndicator(color = MainColor, modifier = Modifier.padding(30.dp))

        }
    }
}

@Composable
fun ProgressBarIndicator() {
    CircularProgressIndicator(color = MainColor, modifier = Modifier.padding(30.dp))
}

@Composable
fun ProgressBarCus(onDismiss: () -> Unit, progress: Float) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                CircularProgressIndicator(
                    color = MainColor, modifier = Modifier.padding(30.dp),
                    progress = progress
                )
                Text(
                    "Loading PDF File...", textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
fun ProgressBarCusIndicator(progress: Float) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                CircularProgressIndicator(
                    color = MainColor, modifier = Modifier.padding(30.dp),
                    progress = progress
                )
                Text(
                    "Loading PDF File...", textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
fun OnNoDataFound(msg: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Warning, "", tint = Color.Yellow)
                Sp(w = 20.dp)
                Text(
                    msg,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}