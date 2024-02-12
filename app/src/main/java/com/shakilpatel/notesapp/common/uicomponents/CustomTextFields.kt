package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.shakilpatel.notesapp.common.MainColor

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    hint: String = "",
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var tf by remember { mutableStateOf(value) }
    TextField(
        value = tf, onValueChange = {
            onTextChanged(it)
            tf = it
        },
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(hint, style = MaterialTheme.typography.bodySmall) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainColor
        )
    )
}

@Composable
fun SignupTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String,
    hint: String = "",
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var tf by remember { mutableStateOf(value) }
    OutlinedTextField(
        value = tf, onValueChange = {
            onTextChanged(it)
            tf = it
        },
        modifier = modifier.fillMaxWidth(.95f).padding(10.dp),
        placeholder = { Text(hint, style = MaterialTheme.typography.bodySmall) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None, // Handle password input
        label = { Text(label, style = MaterialTheme.typography.bodySmall, color = MainColor) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainColor,
            cursorColor = MainColor
        ),
        singleLine = true,

    )
}


@Composable
fun ProfileTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    hint: String = "",
    onTextChanged: (String) -> Unit,
    onSaveClicked: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var tf by remember { mutableStateOf(value) }
    var isEditable by remember { mutableStateOf(false) }
    if (isEditable) {
        TextField(
            value = tf, onValueChange = {
                onTextChanged(it)
                tf = it
            },
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(hint, style = MaterialTheme.typography.bodySmall) },
            trailingIcon = {
                Icon(
                    Icons.Default.Check, "",
                    Modifier.clickable {
                        onSaveClicked(tf)
                        isEditable = false
                    },
                    tint = MainColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            label = { Text(label, style = MaterialTheme.typography.bodySmall) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainColor,
                focusedLabelColor = MainColor
            )
        )
    } else {
        TextField(
            value = tf, onValueChange = {
                onTextChanged(it)
                tf = it
            },
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(hint, style = MaterialTheme.typography.bodySmall) },
            trailingIcon = {

                Icon(
                    Icons.Default.Edit, "",
                    Modifier.clickable {
                        isEditable = true
                    },
                    tint = MainColor
                )
            },
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            label = { Text(label, style = MaterialTheme.typography.bodySmall) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainColor
            )
        )
    }

}

@Composable
fun ProfileTextFieldDisabled(
    label: String,
    modifier: Modifier = Modifier,
    value: String
) {
    OutlinedTextField(
        value = value, onValueChange = {},
        modifier = modifier.fillMaxWidth(),
        readOnly = true,
        label = { Text(label, style = MaterialTheme.typography.titleSmall, color = MainColor) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MainColor,
            unfocusedLabelColor = MainColor,
        )
    )

}
