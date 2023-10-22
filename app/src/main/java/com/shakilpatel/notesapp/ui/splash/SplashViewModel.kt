package com.shakilpatel.notesapp.ui.splash

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.notesapp.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val repo: AuthRepo) : ViewModel() {

    fun checkUserExist(): Boolean {
        return FirebaseAuth.getInstance().uid != null
    }

    fun getUserEmail(): String {
        return FirebaseAuth.getInstance().currentUser?.email!!
    }
}