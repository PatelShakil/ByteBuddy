package com.shakilpatel.notesapp.ui.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.messaging.FirebaseMessaging
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.learning.CollegeModel
import com.shakilpatel.notesapp.data.models.learning.EducationModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.data.repo.AuthRepo
import com.shakilpatel.notesapp.data.repo.CommonRepo
import com.shakilpatel.notesapp.ui.nav.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val commonRepo: CommonRepo,
    private val authRepo: AuthRepo,
    private val auth: FirebaseAuth,
    private val context: Context
) : ViewModel() {

    val notiCount = mutableIntStateOf(0)

    private val _loginResult = MutableStateFlow<Resource<Boolean>?>(null)
    val loginResult = _loginResult

    private val _signupResult = MutableStateFlow<Resource<Boolean>?>(null)
    val signupResult = _signupResult
    init {
        getUnreadNotiCount()
    }
    fun getUnreadNotiCount(uid: String = auth.uid.toString()){
        commonRepo.getUnreadNotiCount(uid){
            notiCount.intValue = it
        }
    }


    var eduList: Resource<List<EducationModel>>? = null

    fun registerUserToken(){
        commonRepo.registerUserToken()
    }


    fun getEducationList(onResult: (List<EducationModel>) -> Unit) {
        commonRepo.getEducationList {
            eduList = it

            when (it) {
                is Resource.Success -> {
                    Log.d("List", it.result.toString())
                    onResult(it.result)
                }

                else -> {
                }
            }
        }
    }


    var collegeList: Resource<List<CollegeModel>>? = null

    fun getCollegeList(onResult: (List<CollegeModel>) -> Unit) {
        commonRepo.getCollegeList {
            collegeList = it

            when (it) {
                is Resource.Success -> {
                    Log.d("List", it.result.toString())
                    onResult(it.result)
                }

                else -> {
                }
            }
        }
    }

    var email = mutableStateOf("")

    //logout
    fun doLogout() {
        authRepo.doLogout()
    }

    var userData: MutableStateFlow<UserModel>? = null


    //getUserData
    fun getUserDataFromUid(uid: String): UserModel {
        authRepo.getUserDataFromUid(uid) {
            when (it) {
                is Resource.Success -> {
                    userData?.value = it.result
                }

                else -> {}
            }
        }
        return userData?.value!!
    }

    //login
    fun login(email: String, pass: String) {
        _loginResult.value = Resource.Loading
        authRepo.doLogin(email,pass){
            _loginResult.value = it
        }
    }
    fun doLogin(email: String, pass: String, navController: NavController) {
        authRepo.doLogin(email, pass) { login ->
            when (login) {
                is Resource.Success -> {
                    isUserExists(FirebaseAuth.getInstance().uid.toString(), navController)
                }

                is Resource.Failure -> {
                    Toast.makeText(context, login.errorMsgBody, Toast.LENGTH_SHORT).show()
                    Log.d("Error Login", login.errorMsgBody)
                }

                else -> {}
            }
        }

    }

    fun isUserExists(): Boolean {
        return authRepo.isUserExists(auth.uid.toString())
    }

    //isUserExists
    val isUserExistsStatus: MutableState<Boolean> = mutableStateOf(false)
    fun isUserExists(uid: String? = auth.uid, navController: NavController) {
        isUserExistsStatus.value = true
        if (uid != null) {
            authRepo.isUserExists(uid) {
                when (it) {
                    is Resource.Success -> {
                        email.value = auth.currentUser?.email.toString()
                        if (it.result) {

                                navController.navigate(Screen.Main.route){
                                    popUpTo(Screen.Auth.route)
                                }

                        } else
                            navController.navigate(Screen.Auth.Signup.route + "/${email.value}")
                    }

                    is Resource.Failure -> {
                        isUserExistsStatus.value = false
                        Log.d("Error Is User Exists", it.errorMsgBody)
                        it.ex.printStackTrace()
                        Toast.makeText(context, it.errorMsgBody, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                    }
                }
            }
        } else {
            isUserExistsStatus.value = false
        }
    }
    //signup

    fun doSignup(user: UserModel,pass: String, navController: NavController) {
        _signupResult.value = Resource.Loading
        authRepo.doSignup(user,pass) {
            _signupResult.value = it
        }
    }

    fun getUserModel(uid: String = auth.uid.toString(), onResult: (UserModel) -> Unit) {
        commonRepo.getUserModel {
            onResult(it)
        }
    }

    fun forgotPassword(email: String)=viewModelScope.launch {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(context, "Password Reset Link Sent", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        } catch (e: FirebaseAuthInvalidUserException) {
            Toast.makeText(context, "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }
}
