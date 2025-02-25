package com.shakilpatel.bytebuddy.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.user.UserModel
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