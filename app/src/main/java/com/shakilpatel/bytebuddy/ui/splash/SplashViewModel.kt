package com.shakilpatel.bytebuddy.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {


    fun isUserExists(onResult: (Boolean) -> Unit) {
        val uid = auth.uid
        Log.d("UID SPLASH", uid ?: "null")

        if (!uid.isNullOrEmpty()) {
            onResult(true)


        } else {
            onResult(false)

        }
    }
}