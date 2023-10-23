package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.ui.auth.AuthViewModel
import com.shakilpatel.notesapp.ui.nav.Screen


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    // for home
    object Home : BottomBarScreen(
        route = Screen.Main.Home.route,
        title = "Home",
        icon = R.drawable.ic_home,
        icon_focused = R.drawable.ic_home
    )

    // for report
    object Feed : BottomBarScreen(
        route = Screen.Main.Feed.Landing.route +"/${"#"}" ,
        title = "Feed",
        icon = R.drawable.ic_feed,
        icon_focused = R.drawable.ic_feed
    )

    // for report
    object Create : BottomBarScreen(
        route = Screen.Main.Create.route,
        title = "Create",
        icon = R.drawable.ic_add,
        icon_focused = R.drawable.ic_add
    )

    // for report
    object Profile : BottomBarScreen(
        route = Screen.Main.Profile.route,
        title = "Profile",
        icon = R.drawable.ic_profile_round_img,
        icon_focused = R.drawable.ic_profile_round_img
    )

}

@Composable
fun BadgeBottomNavigation(navController: NavController, viewModel: AuthViewModel) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Feed,
        BottomBarScreen.Create,
        BottomBarScreen.Profile
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                viewModel
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController,
    viewModel: AuthViewModel
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) MainColor else Color.Transparent

    val contentColor =
        if (selected) Color.White else MainColor

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            var user by remember { mutableStateOf(UserModel()) }
            viewModel.getUserModel {
                user = it
            }
            if (screen.route == Screen.Main.Profile.route) {
                Box(modifier = Modifier.size(24.dp)) {
                    CircularImage(size = 24.dp, image = user.profileImg) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                }
            } else {
                Icon(
                    painter = painterResource(id = if (selected) screen.icon_focused else screen.icon),
                    contentDescription = "icon",
                    tint = contentColor
                )
            }
            AnimatedVisibility(selected) {
                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}

