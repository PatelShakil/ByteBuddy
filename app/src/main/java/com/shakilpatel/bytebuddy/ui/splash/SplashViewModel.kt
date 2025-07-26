package com.shakilpatel.bytebuddy.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {



    fun isUserExists(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        val uid = auth.uid
        Log.d("UID SPLASH", uid ?: "null")

        if (!uid.isNullOrEmpty()) {
            try {
                auth.currentUser?.reload()?.await()
                onResult(true)
            } catch (e: FirebaseAuthInvalidUserException) {
                if (e.errorCode == "user-disabled") { // Check specific error code
                    println("User account disabled, signing out...")
                    auth.signOut()
                    onResult(false)
                    // Navigate to login screen or display appropriate message
                } else {
                    println("Error reloading user: ${e.message}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Error reloading user: ${e.message}")
                onResult(false)
            }

        } else {
            onResult(false)

        }
    }
}