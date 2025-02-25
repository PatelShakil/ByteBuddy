package com.shakilpatel.bytebuddy.data.repo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.error.FAQModel
import com.shakilpatel.bytebuddy.data.models.learning.CollegeModel
import com.shakilpatel.bytebuddy.data.models.learning.EducationModel
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.user.SavedModelExp
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import javax.inject.Inject

class CommonRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {


    fun getUnreadNotiCount(uid:String,onResult:(Int) ->Unit){
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if(value != null && value.exists()){
                    val userModel = value.toObject(UserModel::class.java)!!
                    onResult(userModel.notifications.filter { !it.read }.size)
                }
            }
    }

    fun checkUserIsOnline(uid: String, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    if (value.exists()) {
                        val user = value.toObject(UserModel::class.java)!!
                        onResult(user.online)
                    }
                }
            }
    }

    fun registerUserToken(){
        val userRef = db.collection("users")
            .document(auth.uid.toString())
        userRef.get().addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(UserModel::class.java)!!
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        user.token = it.toString()
                        userRef.set(user)
                            .addOnSuccessListener {
                                Log.d("Token",user.token)
                            }
                    }

                }
            }
    }

    fun getUserNameFromUid(uid: String,callback:(String) -> Unit){
        val db = Firebase.firestore
        val documentReference = db.collection("users").document(uid)
        documentReference.get()
            .addOnSuccessListener {
                if (it.exists()){
                   callback(it.data?.get("name").toString())
                }
            }
    }

    fun getTokenFromUid(uid: String,callback: (String) -> Unit){
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val user = it.toObject(UserModel::class.java)
                    if(user != null){
                        callback(user.token)
                    }
                }
            }
    }

    fun getEducationList(onResult: (Resource<ArrayList<EducationModel>>) -> Unit) {
        onResult(Resource.Loading)
        database.reference.child("education")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<EducationModel>()
                    if (snapshot.exists()) {
                        for (e in snapshot.children) {
                            list.add(e.getValue<EducationModel>()!!)
                        }

                        onResult(Resource.Success(list))

                    } else {
                        onResult(Resource.Failure(Exception("No Data Available"), "Data not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(Resource.Failure(error.toException(), error.message))
                }
            })

    }

    fun getCollegeList(onResult: (Resource<ArrayList<CollegeModel>>) -> Unit) {
        onResult(Resource.Loading)
        database.reference.child("colleges")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<CollegeModel>()
                    if (snapshot.exists()) {
                        for (e in snapshot.children) {
                            list.add(e.getValue<CollegeModel>()!!)
                        }
                        Log.d("List", list.toString())
                        onResult(Resource.Success(list))

                    } else {
                        onResult(Resource.Failure(Exception("No Data Available"), "Data not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(Resource.Failure(error.toException(), error.message))
                }
            })

    }


    fun getUploads(uid: String, onResult: (Resource<SavedModelExp>) -> Unit) {
        onResult(Resource.Loading)
        val saved = mutableStateOf(SavedModelExp())
        db.collection("notes")
            .where(Filter.equalTo("author", uid))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onResult(Resource.Failure(error, error.localizedMessage))
                    return@addSnapshotListener
                }
                if (value != null) {
                    val notesList = ArrayList<NotesModel>()
                    if (!value.isEmpty) {
                        for (notes in value.documents) {
                            notesList.add(notes.toObject(NotesModel::class.java)!!)
                        }
                        saved.value.notes = notesList
                        onResult(Resource.Success(saved.value))
                    }
                }
            }
        db.collection("faq")
            .where(Filter.equalTo("userId", uid))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onResult(Resource.Failure(error, error.localizedMessage))
                    return@addSnapshotListener
                }
                if (value != null) {
                    val errorsList = ArrayList<FAQModel>()
                    if (!value.isEmpty) {
                        for (post in value.documents) {
                            errorsList.add(post.toObject(FAQModel::class.java)!!)
                        }
                        saved.value.errors = errorsList
                        onResult(Resource.Success(saved.value))
                    }
                }
            }
    }

    fun getSaved(uid: String, onResult: (Resource<SavedModelExp>) -> Unit) {
        val saved = mutableStateOf(SavedModelExp())
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (value != null && value.exists()) {
                    val user = value.toObject(UserModel::class.java)!!
                    val notesList = ArrayList<NotesModel>()
                    notesList.clear()
                    user.saved.notes.forEach {
                        getNotesModel(id = it) {
                            when (it) {
                                is Resource.Success -> {
                                    notesList.add(it.result)
                                    saved.value.notes = notesList
                                    onResult(Resource.Success(saved.value))
                                }

                                else -> {}
                            }
                        }
                    }
                    val errorsList = ArrayList<FAQModel>()
                    errorsList.clear()
                    user.saved.errors.forEach {
                        db.collection("faq")
                            .document(it)
                            .addSnapshotListener { value, error ->
                                if (value != null) {
                                    if (value.exists()) {
                                        errorsList.add(value.toObject(FAQModel::class.java)!!)
                                        saved.value.errors = errorsList
                                        onResult(Resource.Success(saved.value))
                                    }

                                }

                            }
//                getErrorModel(id = it) {
//                    when (it) {
//                        is Resource.Success -> {
//                            errorsList.add(it.result)
//                            saved.value.errors = errorsList
//                            onResult(Resource.Success(saved.value))
//                        }
//
//                        else -> {}
//                    }
//                }
                    }
                }


            }
    }

    fun getErrorModel(id: String, getError: (Resource<FAQModel>) -> Unit) {
        db.collection("faq")
            .document(id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    if (value.exists()) {
                        getError(Resource.Success(value.toObject(FAQModel::class.java)!!))
                    }
                }
            }

    }

    fun getUserModel(
        uid: String = FirebaseAuth.getInstance().uid.toString(),
        getUser: (UserModel) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    if (value.exists()) {
                        getUser(value.toObject(UserModel::class.java)!!)
                    }
                }
            }
    }

    fun getNotesModel(id: String, getNotes: (Resource<NotesModel>) -> Unit) {
        db.collection("notes")
            .document(id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    if (value.exists()) {
                        getNotes(Resource.Success(value.toObject(NotesModel::class.java)!!))
                    }
                }
            }
    }
}