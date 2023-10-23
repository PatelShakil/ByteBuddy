package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shakilpatel.notesapp.common.MainColor

@Composable
fun SearchBar(hint: String,searchStr:String = "", onTextChanged: (String) -> Unit) {
    var value by remember { mutableStateOf(searchStr) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColor),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(vertical = 15.dp),
            border = BorderStroke(.5.dp, Color.Gray),
            elevation = CardDefaults.cardElevation(
                18.dp
            )
        ) {
            TextField(
                value = value, onValueChange = {
                    value = it
                    onTextChanged(value.lowercase().trim())
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search, contentDescription = "",
                        tint = MainColor
                    )
                },
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        Icon(
                            Icons.Default.Clear, "",
                            tint = MainColor,
                            modifier = Modifier.clickable {
                                value = ""
                                onTextChanged(value)
                            }
                        )
                    }
                },
                label = {
                    Text(
                        hint,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.White,
                    cursorColor = MainColor
                ),
                singleLine = true
            )
        }
    }

}