package com.shakilpatel.bytebuddy.common.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.easy.translator.EasyTranslator
import com.easy.translator.LanguagesModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.RedColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun UpdateDialogue(latestVersion: String, onBack: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier= Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(15)
        ){
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ){
                val openUri = LocalUriHandler.current

                Image(
                    painterResource(id = R.drawable.play_logo),
                    ""
                )
                Spacer(Modifier.height(10.dp))

                var message by remember { mutableStateOf("") }
                FirebaseDatabase.getInstance().reference.child("message")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                message = snapshot.value.toString()
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                Text(message)


                Spacer(Modifier.height(10.dp))

                Row(
                    modifier= Modifier.fillMaxWidth()
                ){
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            RedColor
                        ),
                        modifier= Modifier.weight(1f)
                    ){
                        Text("Close")
                    }
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = {
                            FirebaseDatabase.getInstance().reference.child("updateLink")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            openUri.openUri(snapshot.value.toString())
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF5FAC65)
                        ),
                        modifier= Modifier.weight(1f)
                    ){
                        Text("Update")
                    }

                }

            }
        }
    }
}

@Composable
fun TranslateDialog(onSuccess: (DropDownItemData) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val translator = EasyTranslator(context)
    var data by remember { mutableStateOf(DropDownItemData()) }
    var isTransLoading by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(modifier = Modifier.fillMaxWidth(.8f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HorizontalBrush),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sp(h = 5.dp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_translate),
                        contentDescription = "",
                        tint = MainColor
                    )
                    Text("Translate")
                }
                Sp(h = 5.dp)
                Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.fillMaxWidth())

                CusDropdownSearch(
                    selectedItem = DropDownItemData(
                        LanguagesModel.ENGLISH.shortCode,
                        LanguagesModel.ENGLISH.langName
                    ),
                    label = "Language",
                    hint = "Search your language",
                    options = translator.getLanguagesList()
                        .map { DropDownItemData(it.shortCode, it.langName) },
                    onSelected = {
                        // Clear previous translations
                        data = it
                    }
                )
                Sp(h = 5.dp)
                Sp(h = 10.dp)
                if (isTransLoading) {
                    CircularProgressIndicator(color = MainColor, modifier = Modifier.padding(30.dp))
                }
                Sp(h = 10.dp)
                CustomOutlinedButton(
                    label = "Translate Now",
                    isEnable = data != DropDownItemData()
                ) {
                    onSuccess(data)
                    isTransLoading = true
                }
                Sp(h = 5.dp)
            }
        }
    }
}

@Composable
fun SnackBarCus(msg: String) {
    val coroutineScope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        // Show the Snackbar
        coroutineScope.launch {
            isVisible = true
        }

        // Delay for 3 to 5 seconds and then hide the Snackbar
        delay(3000L)
        coroutineScope.launch {
            isVisible = false
        }
    }
    if (isVisible) {
        Dialog(onDismissRequest = {
            isVisible = false
        }) {
            Snackbar(
                modifier = Modifier
                    .padding(8.dp),
                action = {}
            ) {
                Text(text = msg, color = Color.White)
            }
        }

    }
}

@Composable
fun ConfirmationDialog(msg: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        text = {},
        title = {
            Text(msg)
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        })
}