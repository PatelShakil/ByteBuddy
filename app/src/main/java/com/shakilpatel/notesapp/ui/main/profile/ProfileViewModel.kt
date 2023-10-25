package com.shakilpatel.notesapp.ui.main.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.learning.CollegeModel
import com.shakilpatel.notesapp.data.models.learning.EducationModel
import com.shakilpatel.notesapp.data.models.user.SavedModelExp
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.data.repo.CommonRepo
import com.shakilpatel.notesapp.data.repo.ProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val commonRepo: CommonRepo,
    private val profileRepo: ProfileRepo,
    private val auth: FirebaseAuth,
    val database: FirebaseDatabase
) : ViewModel() {

    val aboutMsg = mutableStateOf("")

    init {
        database.reference.child("aboutMsg")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                        aboutMsg.value = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    var updateResult: MutableState<Boolean?> = mutableStateOf(null)

    fun updateProfile(user: UserModel) {
        profileRepo.updateProfile(user) {
            when (it) {
                is Resource.Success -> {
                    updateResult.value = it.result
                }

                is Resource.Failure -> {
                    updateResult.value = false
                    Log.d("Update Profile Error", it.errorMsgBody)
                }

                else -> {}
            }
        }
    }


    var eduList: Resource<List<EducationModel>>? = null

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

    private val _uploads = MutableStateFlow<Resource<SavedModelExp>?>(null)
    val uploads: MutableStateFlow<Resource<SavedModelExp>?>
        get() = _uploads

    fun getUploads(uid: String = FirebaseAuth.getInstance().uid.toString()) {
        commonRepo.getUploads(uid) {
            _uploads.value = it
        }
    }

    private val _saved = MutableStateFlow<Resource<SavedModelExp>?>(null)
    val saved: MutableStateFlow<Resource<SavedModelExp>?>
        get() = _saved

    fun getSaved(uid: String = FirebaseAuth.getInstance().uid.toString()) {
        _saved.value = Resource.Loading
        commonRepo.getSaved(uid) {
            when (it) {
                is Resource.Success -> {
                    _saved.value = it
//                    Log.d("Saved", _saved.value.toString())
                }

                else -> {}
            }
        }
    }


    val user = MutableStateFlow<Resource<UserModel>?>(null)

    fun getUserModel() {
        commonRepo.getUserModel {
            user.value = Resource.Success(it)
        }
    }

    init {
        commonRepo.getUserModel {
            user.value = Resource.Success(it)
        }
    }

    fun getUploads(uid: String = auth.uid.toString(), onResult: (SavedModelExp) -> Unit) {
        commonRepo.getUploads(uid) {
            when (it) {
                is Resource.Success -> {
                    onResult(it.result)
                }

                else -> {}
            }
        }
    }

}