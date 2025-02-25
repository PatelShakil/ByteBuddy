package com.shakilpatel.bytebuddy.ui.main.feed.error

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.error.FAQAnsModel
import com.shakilpatel.bytebuddy.data.models.error.FAQModel
import com.shakilpatel.bytebuddy.data.models.user.SavedModelExp
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.data.repo.CommonRepo
import com.shakilpatel.bytebuddy.data.repo.ErrorRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FAQViewModel @Inject constructor(
    private val errorRepo: ErrorRepo,
    private val commonRepo: CommonRepo,
    private val auth: FirebaseAuth,
    private val context: Context
) : ViewModel() {


    fun uploadFAQAns(faqAns: FAQAnsModel) {
        errorRepo.uploadFAQAns(faqAns) {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, "Answer uploaded successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Failure -> {
                    Toast.makeText(context, it.errorMsgBody, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    val error = MutableStateFlow<Resource<FAQModel>?>(null)
    fun getError(id: String, onResult: (FAQModel) -> Unit) = viewModelScope.launch {
        commonRepo.getErrorModel(id) {
            when (it) {
                is Resource.Success -> {
                    onResult(it.result)

                }

                else -> {}
            }
            error.value = it
        }
    }

    fun getUserModel(uid: String = auth.uid.toString(), getUser: (UserModel) -> Unit) {
        commonRepo.getUserModel(uid) {
            getUser(it)
        }
    }

    private val _errorsList = MutableStateFlow<Resource<List<FAQModel>>?>(null)
    val errorsList: MutableStateFlow<Resource<List<FAQModel>>?>
        get() = _errorsList

    init {
        getErrorsList()
    }

    fun getErrorsList() = viewModelScope.launch {
        errorRepo.getAllErrorsList {
            _errorsList.value = it
        }
    }

    val errorsListCustom = MutableStateFlow<MutableList<FAQModel>>(mutableListOf())
    fun getErrorsList(list: List<String>) = viewModelScope.launch {
        errorsListCustom.value.clear()
        list.forEach {
            getError(it) {
                errorsListCustom.value.add(it)
                errorsListCustom.value.toMutableList()
            }
        }
        Log.d("List", errorsListCustom.value.toString())


//        errorsList.asStateFlow().value.let {
//            when (it) {
//                is Resource.Success -> {
//                    list.forEach { value ->
//                        errorsListCustom.value?.add(it.result.filter { it.id == value }[0])
//                    }
//                    Log.d("ErrorsListCustom", errorsListCustom.value.toString())
//                }
//
//                else -> {
//                    Log.d("ErrorsListCustom", errorsListCustom.value.toString())
//                }
//            }
//        }
//        errorRepo.getAllErrorsList {
//            _errorsList.value = it
//        }
    }

    private val _errorSaveResult = MutableStateFlow<Resource<Boolean>?>(null)
    val errorSaveResult: MutableStateFlow<Resource<Boolean>?>
        get() = _errorSaveResult

    fun saveError(id: String, uid: String = FirebaseAuth.getInstance().uid.toString()) =
        viewModelScope.launch {
            errorRepo.saveError(id, uid) {
                _errorSaveResult.value = it
                getUserModel {
                    getErrorsList(it.saved.errors)
                }
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
                    Log.d("Saved", _saved.value.toString())
                }

                else -> {}
            }
        }
    }

    fun getUploads(uid: String = auth.uid.toString(), onResult: (List<FAQModel>) -> Unit) {
        commonRepo.getUploads(uid) {
            when (it) {
                is Resource.Success -> {
                    onResult(it.result.errors)
                }

                else -> {}
            }
        }
    }

    fun onUpVote(ans: FAQAnsModel) {
        errorRepo.onUpVote(ans)
    }

    fun onDownVote(ans: FAQAnsModel) {
        errorRepo.onDownVote(ans)
    }


}