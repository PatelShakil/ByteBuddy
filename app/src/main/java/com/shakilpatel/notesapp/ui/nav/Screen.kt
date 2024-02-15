package com.shakilpatel.notesapp.ui.nav

sealed class Screen(val route: String, var title: String = "") {

    object Auth : Screen("auth") {
        object Login : Screen("login")
        object Signup : Screen("screen")

    }

    object Main : Screen("main") {
        object Home : Screen("home") {
            object Landing : Screen("home_landing", "Home")
            object Subject : Screen("subject")
            object Notes : Screen("notes")
        }

        object Feed : Screen("feed") {
            object Landing : Screen("feed_landing", "FAQ")
        }

        object Create : Screen("create") {
            object Landing : Screen("create_landing")
        }

        object Profile : Screen("profile") {
            object Landing : Screen("profile_landing", "Profile")
            object Saved : Screen("profile_saved", "Saved")
            object Uploaded : Screen("profile_uploaded", "Uploads")
            object AboutUs : Screen("profile_aboutus","About Us")
        }

        object Chat : Screen("chat"){
            object Landing : Screen("chat_landing","Chats")

            object ChatUser : Screen("chat_user")
            object AddChatUser : Screen("add_chat_user")
        }
    }

    object ViewNotes : Screen("view_notes") {
        object Landing : Screen("view_notes_landing")
    }

    object Splash : Screen("splash")
    object UsersProfile : Screen("users_profile")
    object Notification : Screen("notification","Notification")
}