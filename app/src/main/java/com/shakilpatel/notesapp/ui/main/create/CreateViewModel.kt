package com.shakilpatel.notesapp.ui.main.create

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.data.models.learning.CourseModel
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.repo.ErrorRepo
import com.shakilpatel.notesapp.data.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val notesRepo: NotesRepo,
    private val errorRepo: ErrorRepo
) : ViewModel() {


    private val _courses = MutableStateFlow<Resource<List<CourseModel>>?>(null)
    val courses: MutableStateFlow<Resource<List<CourseModel>>?>
        get() = _courses

    fun getCoursesList() = viewModelScope.launch {
        _courses.value = Resource.Loading
        notesRepo.getCoursesList {
            _courses.value = it
        }
    }

    private val _uploadResult = MutableStateFlow<Resource<Boolean>?>(null)
    val uploadResult: MutableStateFlow<Resource<Boolean>?>
        get() = _uploadResult

    fun uploadNotes(context: Context, notes: NotesModel) = viewModelScope.launch {
        _uploadResult.value = Resource.Loading
        notesRepo.uploadNotes(context, notes) {
            _uploadResult.value = it
        }
    }


    private val _uploadErrorResult = MutableStateFlow<Resource<Boolean>?>(null)
    val uploadErrorResult: MutableStateFlow<Resource<Boolean>?>
        get() = _uploadErrorResult

    fun uploadError(error: FAQModel) = viewModelScope.launch {
        _uploadErrorResult.value = Resource.Loading
        errorRepo.uploadError(error) {
            _uploadErrorResult.value = it
        }
    }
}