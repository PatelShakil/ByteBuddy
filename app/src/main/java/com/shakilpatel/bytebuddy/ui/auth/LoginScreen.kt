package com.shakilpatel.bytebuddy.ui.auth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.MainColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.uicomponents.CustomOutlinedButton
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarCus
import com.shakilpatel.bytebuddy.common.uicomponents.SignupTextField
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController, onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
    ByteBuddyTheme {
        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var isForgotPasswordClicked by remember { mutableStateOf(false) }


        if (viewModel.isUserExistsStatus.value) {
            ProgressBarCus {

            }
        } else {
            Box {
                Image(
                    painterResource(R.drawable.login_bg),
                    "",
                    modifier= Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(HorizontalBrush)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Sp(70.dp)
                    Card(
                        modifier = Modifier, shape = CircleShape, colors = CardDefaults.cardColors(
                            Color.Transparent
                        )
                    ) {
                        Image(
                            painter = painterResource(R.mipmap.ic_launcher_foreground), "",
                            modifier = Modifier
                                .size(120.dp)
                                .scale(1.3f),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Sp(h = 20.dp)
                    Text(
                        "Welcome to ByteBuddy",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Cursive
                    )
                    Sp(h = 40.dp)
                    SignupTextField(
                        value = email, label = "Email", onTextChanged = {
                            email = it
                        },
                        keyboardType = KeyboardType.Email
                    )
                    Sp(h = 15.dp)
                    SignupTextField(
                        label = "Password", onTextChanged = {
                            pass = it
                        },
                        keyboardType = KeyboardType.Password
                    )
                    Sp(h = 10.dp)
                    Text("Forgot Password ?",
                        modifier= Modifier.align(Alignment.End)
                            .padding(end = 20.dp)
                            .clickable{
                                isForgotPasswordClicked = true
                            },
                        style = MaterialTheme.typography.titleSmall)
                    Sp(h = 25.dp)
                    CustomOutlinedButton(
                        label = "Login",
                        isEnable = email.isNotEmpty() && pass.isNotEmpty()
                    ) {
                        viewModel.login(email.trim(), pass.trim())
                    }
                    Sp(h = 60.dp)

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account ? ")
                        Text("Signup",
                            color = MainColor,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Auth.Signup.route)
                            })
                    }

                    val context = LocalContext.current
                    viewModel.loginResult.collectAsState().value.let {
                        when (it) {
                            is Resource.Loading -> {
                                ProgressBarCus {

                                }
                            }

                            is Resource.Failure -> {
                                LaunchedEffect(key1 = true) {
                                    Toast.makeText(context, it.errorMsgBody, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            is Resource.Success -> {
                                LaunchedEffect(key1 = true) {
                                    if (it.result) {
                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(Screen.Auth.Login.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                    if (isForgotPasswordClicked){
                        ForgotPasswordDialog(viewModel){
                            isForgotPasswordClicked = false
                        }
                    }

                }






            }
        }
    }
}


@Composable
fun ForgotPasswordDialog(viewModel: AuthViewModel,onDismiss: ()->Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(15.dp)
                ),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Forgot Password",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(20.dp))
                var email by remember {
                    mutableStateOf("")
                }

                SignupTextField(
                    value = email, label = "Email",
                    hint = "Enter Your Registered Email",
                    onTextChanged = {
                        email = it.trim()
                    },
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        viewModel.forgotPassword(email)
                    },
                    enabled = email.isNotEmpty()
                ) {
                    Text("Send Reset Password Link")
                    Color(0xFF00ACC5)
                }

            }
        }

    }
}
