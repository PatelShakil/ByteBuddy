package com.shakilpatel.notesapp.ui.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.notesapp.common.uicomponents.ProgressBarCus
import com.shakilpatel.notesapp.common.uicomponents.SignupTextField
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController, onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
    ByteBuddyTheme {
        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        LaunchedEffect(true) {
            viewModel.isUserExists(navController = navController)
        }

        if (viewModel.isUserExistsStatus.value) {
            ProgressBarCus {

            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HorizontalBrush)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Sp(70.dp)
                Card(modifier = Modifier.size(100.dp), shape = CircleShape) {
                    Image(
                        painter = painterResource(R.drawable.logo), "",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Sp(h = 20.dp)
                Text(
                    "Welcome to ByteBuddy",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Cursive
                )
                Sp(h = 50.dp)
                SignupTextField(value = email, label = "Email", onTextChanged = {
                    email = it
                })
                SignupTextField(
                    value = pass, label = "Password", onTextChanged = {
                        pass = it
                    },
                    keyboardType = KeyboardType.Password
                )
                Sp(h = 20.dp)
                CustomOutlinedButton(
                    label = "Login",
                    isEnable = email.isNotEmpty() && pass.isNotEmpty()
                ) {
                    viewModel.doLogin(email.trim(), pass.trim(), navController)
                }
                Sp(h = 200.dp)

            }
        }
    }
}