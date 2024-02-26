package com.shakilpatel.notesapp.ui.splash

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.repo.AuthRepo
import com.shakilpatel.notesapp.ui.nav.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth : FirebaseAuth
) : ViewModel() {


    fun isUserExists( navController: NavController) {
        val uid = auth.uid.toString()

        if(uid.isNotEmpty()){
            navController.navigate(Screen.Main.route){
                popUpTo(Screen.Splash.route){
                    inclusive = true
                }
            }
        }else{
            navController.navigate(Screen.Auth.Login.route){
                popUpTo(Screen.Splash.route){
                    inclusive = true
                }
            }
        }
    }
}