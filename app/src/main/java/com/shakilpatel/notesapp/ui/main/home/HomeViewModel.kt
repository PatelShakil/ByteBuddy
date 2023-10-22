package com.shakilpatel.notesapp.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.learning.CourseModel
import com.shakilpatel.notesapp.data.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notesRepo: NotesRepo
) : ViewModel() {

    init {
        getCoursesList()
    }

    private val _courses = MutableStateFlow<Resource<List<CourseModel>>?>(null)
    val courses: MutableStateFlow<Resource<List<CourseModel>>?>
        get() = _courses

    fun getCoursesList() = viewModelScope.launch {
        notesRepo.getCoursesList {
            _courses.value = it
        }
    }


}