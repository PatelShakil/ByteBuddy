package com.shakilpatel.notesapp.ui.main.usersprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.user.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersProfileViewModel @Inject constructor(
    private val db : FirebaseFirestore
) : ViewModel(){


    private val _user = MutableStateFlow<Resource<UserModel>?>(null)
    val user = _user

    fun getUser(id : String)=viewModelScope.launch {
        _user.value = Resource.Loading
        db.collection("users").document(id).get().addOnSuccessListener {
            _user.value = Resource.Success(it.toObject(UserModel::class.java)!!)
        }
            .addOnFailureListener {
                _user.value = Resource.Failure(errorMsgBody = it.message.toString(), ex = it)
            }
    }




}