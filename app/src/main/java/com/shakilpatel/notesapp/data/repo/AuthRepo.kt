package com.shakilpatel.notesapp.data.repo

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.Cons.Companion.USERS
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.user.UserModel
import java.lang.Exception
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val context: Context
) {

    fun doLogout() {
        auth.signOut()
    }

    fun doLogin(email: String, password: String, loginResultCallback: (Resource<Boolean>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginResultCallback(Resource.Success(true))
                }
                if (it.exception != null) {
                    loginResultCallback(
                        Resource.Failure(
                            it.exception!!,
                            it.exception!!.localizedMessage
                        )
                    )
                }
            }
    }

    fun isUserExists(uid: String): Boolean {
        return db.collection(USERS)
            .document(uid)
            .get()
            .isSuccessful
    }

    fun isUserExists(uid: String, existsResultCallback: (Resource<Boolean>) -> Unit) {
        if (Cons.isInternetConnected(context)) {
            db.collection(USERS)
                .document(uid)
                .get()
                .addOnCompleteListener {
                    if (it.result.exists()) {
                        existsResultCallback(Resource.Success(true))
                    } else {
                        existsResultCallback(Resource.Success(false))
                    }
                    if (it.exception != null) {
                        existsResultCallback(
                            Resource.Failure(
                                it.exception!!,
                                it.exception!!.localizedMessage
                            )
                        )
                    }
                }
        } else {
            existsResultCallback(
                Resource.Failure(
                    Exception("Connect to internet first"),
                    "please connect internet"
                )
            )
        }

    }

    fun getUserDataFromUid(uid: String, userResultCallback: (Resource<UserModel>) -> Unit) {
        db.collection(USERS)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    userResultCallback(Resource.Failure(error, error.localizedMessage))
                }
                if (value != null) {
                    if (value.exists()) {
                        userResultCallback(Resource.Success(value.toObject(UserModel::class.java)!!))
                    }
                }
            }
    }

    fun doSignup(user: UserModel,pass: String, signupResultCallback: (Resource<Boolean>) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, pass)
            .addOnSuccessListener {
                if(it.user != null) {
                    user.uid = it.user?.uid.toString()
                    db.collection("users")
                        .document(it?.user?.uid!!)
                        .set(user)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                signupResultCallback(Resource.Success(true))
                            } else {
                                signupResultCallback(Resource.Success(false))
                            }
                            if (it.exception != null) {
                                signupResultCallback(
                                    Resource.Failure(
                                        it.exception!!,
                                        it.exception!!.localizedMessage
                                    )
                                )
                            }
                        }
                }
            }
            .addOnFailureListener {
                signupResultCallback(
                    Resource.Failure(
                        it,
                        it.localizedMessage
                    )
                )
            }
    }
}