package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shakilpatel.notesapp.common.AppBarTextColor
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.TextColor
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.ui.auth.AuthViewModel
import com.shakilpatel.notesapp.ui.nav.Screen

@Preview
@Composable
fun AppBarCallingFun() {
//    CusAppBar(rememberNavController())
}

@Composable
fun CusAppBar(navController: NavController, viewModel: AuthViewModel) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val title: String = when (navBackStackEntry.value?.destination?.route) {
        // Auth Screens
        Screen.Auth.Login.route -> Screen.Auth.Login.title
        Screen.Auth.Signup.route -> Screen.Auth.Signup.title

        // Main Screens
        Screen.Main.Home.Landing.route -> Screen.Main.Home.Landing.title
        Screen.Main.Feed.Landing.route -> Screen.Main.Feed.Landing.title
        Screen.Main.Create.Landing.route -> Screen.Main.Create.Landing.title
        Screen.Main.Profile.Landing.route -> Screen.Main.Profile.Landing.title
        Screen.Main.Profile.Uploaded.route -> Screen.Main.Profile.Uploaded.title
        Screen.Main.Profile.Saved.route -> Screen.Main.Profile.Saved.title

        // ViewNotes Screens
        Screen.ViewNotes.Landing.route -> Screen.ViewNotes.Landing.title

        else -> ""
    }
    when (navBackStackEntry.value?.destination?.route) {

        Screen.Main.Profile.Uploaded.route -> {
            DefaultAppBar(title = title, navController = navController, viewModel = viewModel)

        }

        Screen.Main.Profile.Saved.route -> {
            DefaultAppBar(title = title, navController = navController, viewModel = viewModel)

        }

        Screen.Main.Profile.Landing.route -> {
            DefaultAppBar(title = title, navController = navController, viewModel = viewModel)

        }

        Screen.Main.Home.Landing.route -> {
            DefaultAppBar(title = title, navController = navController, viewModel = viewModel)

        }

        else -> {
        }
    }
}

@Composable
fun DefaultAppBar(title: String, navController: NavController, viewModel: AuthViewModel) {
    var isLogoutClicked by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = Modifier.height(40.dp), title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = AppBarTextColor
            )
        },
        navigationIcon = {
            Sp(w = 10.dp)
            if (navController.currentBackStackEntryAsState().value?.destination?.route != Screen.Main.Home.Landing.route) {
                Icon(Icons.Default.ArrowBack, "", tint = White,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
            }
        },
        actions = {
            Icon(Icons.Default.ExitToApp, "",
                tint = WhiteColor,
                modifier = Modifier.clickable {
                    isLogoutClicked = !isLogoutClicked

                })
        },
        backgroundColor = MainColor
    )
    if (isLogoutClicked) {
        ConfirmationDialog(msg = "Are you sure want to logout?", onDismiss = {
            isLogoutClicked = !isLogoutClicked
        }) {
            viewModel.doLogout()
            navController.navigate(Screen.Auth.route) {
                popUpTo(navController.graph.findStartDestination().id)
            }
            isLogoutClicked = !isLogoutClicked
        }
    }
}

@Composable
fun NotesAppBar(
    title: String,
    navController: NavController,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier, title = {
            Text(
                title,
                color = AppBarTextColor
            )
        },
        navigationIcon = {
            Sp(w = 10.dp)
            Icon(Icons.Default.ArrowBack, "", tint = White,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                })
        },
        actions = actions,
        backgroundColor = MainColor
    )
}

@Composable
fun FAQAppBar(
    title: String,
    resultCount: Int,
    navController: NavController,
    onTextChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
//        TopAppBar(
//            modifier = Modifier.fillMaxWidth(),
//            navigationIcon = {
//                Sp(w = 10.dp)
//                Icon(Icons.Default.ArrowBack, "", tint = White,
//                    modifier = Modifier.clickable {
//                        navController.popBackStack()
//                    })
//            },
//            title = {
//                Text(title, color = TextColor)
//            },
//            backgroundColor = MainColor
//        )
        SearchBar(hint = "Search your doubts/questions here", onTextChanged = {
            onTextChanged(it)
        })
        AnimatedVisibility(visible = resultCount != 0) {
            Text(
                text = "$resultCount Questions found",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainColor)
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Center,
                color = TextColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
fun SubjectAppBar(title: String, navController: NavController) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            Sp(w = 10.dp)
            Icon(Icons.Default.ArrowBack, "", tint = White,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                })
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = AppBarTextColor
            )

        },
        backgroundColor = MainColor
    )
}
