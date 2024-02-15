package com.shakilpatel.notesapp.ui.nav

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.notesapp.common.uicomponents.BadgeBottomNavigation
import com.shakilpatel.notesapp.common.uicomponents.CusAppBar
import com.shakilpatel.notesapp.ui.main.usersprofile.UsersProfileScreen
import com.shakilpatel.notesapp.ui.nav.navigations.AuthNav
import com.shakilpatel.notesapp.ui.nav.navigations.MainNav
import com.shakilpatel.notesapp.ui.nav.navigations.NotiNav
import com.shakilpatel.notesapp.ui.notification.NotificationScreen
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@Composable
fun NotesAppNavHost(onBack: () -> Unit) {
    val navController = rememberNavController()

    var isAppbarVisible by remember { mutableStateOf(false) }
    var isBottomNavVisible by remember { mutableStateOf(false) }
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    when (navBackStackEntry.value?.destination?.route) {
        Screen.Main.Feed.Landing.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }

        Screen.Main.Profile.Uploaded.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }

        Screen.Main.Profile.Saved.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }

        Screen.Main.Profile.Landing.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }

        Screen.Main.Home.Landing.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }

        Screen.Main.Create.Landing.route -> {
            isAppbarVisible = true
            isBottomNavVisible = true
        }


        else -> {
            isAppbarVisible = true
            isBottomNavVisible = false
        }
    }
    ByteBuddyTheme {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    isBottomNavVisible,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    BadgeBottomNavigation(navController = navController, hiltViewModel())
                }
            },
            topBar = {
                AnimatedVisibility(
                    isAppbarVisible,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    CusAppBar(navController, hiltViewModel())
                }
            }
        ) {
            Log.d("UID",FirebaseAuth.getInstance().uid.toString())

            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = Screen.Auth.route
            ) {
                AuthNav(navController) {
                    onBack()
                }
                MainNav(navController) {
                    onBack()
                }
//                NotiNav(navController)
                composable(Screen.Notification.route,
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "bytebuddy://example.com/notification"
                        }
                    )
                ){
                    NotificationScreen(viewModel = hiltViewModel(), hiltViewModel(), navController = navController)
                }
                composable(
                    Screen.UsersProfile.route + "/{uid}",
                    arguments = listOf(
                        navArgument("uid"){
                            type = NavType.StringType
                        }
                    )
                ){
                    if(it.arguments != null) {
                        UsersProfileScreen(uid = it.arguments?.getString("uid")!!, viewModel = hiltViewModel(), navController =navController)
                    }else{
                        navController.popBackStack()
                    }

                }
            }
        }
    }

}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
