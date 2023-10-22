package com.shakilpatel.notesapp.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.user.UserModel
import javax.inject.Inject

class ProfileRepo @Inject constructor(private val db : FirebaseFirestore) {

    fun updateProfile(user : UserModel,onResult:(Resource<Boolean>)->Unit){
        onResult(Resource.Loading)
        db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onResult(Resource.Success(true))
                }
                if(it.exception != null){
                    onResult(Resource.Failure(it.exception!!,it.exception?.localizedMessage!!))
                }
            }
    }


}