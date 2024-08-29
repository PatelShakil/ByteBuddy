package com.shakilpatel.notesapp.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.learning.CourseModel
import com.shakilpatel.notesapp.data.models.utility.BannerModel
import com.shakilpatel.notesapp.data.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notesRepo: NotesRepo,
    private val db : FirebaseFirestore
) : ViewModel() {

    init {
        getHomeBanners()
        getCoursesList()
    }

    private val _courses = MutableStateFlow<Resource<List<CourseModel>>?>(null)
    val courses: MutableStateFlow<Resource<List<CourseModel>>?>
        get() = _courses

    private val _banners = MutableStateFlow<Resource<List<BannerModel>>?>(null)
    val banners: MutableStateFlow<Resource<List<BannerModel>>?>
        get() = _banners



    fun getCoursesList() = viewModelScope.launch {
        notesRepo.getCoursesList {
            _courses.value = it
        }
    }

    fun getHomeBanners() = viewModelScope.launch {
        db.collection("banners")
            .whereEqualTo("is_active",true)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    _banners.value = Resource.Failure(ex = error, errorMsgBody = error.message.toString())
                    return@addSnapshotListener
                }
                if(value != null) {
                    val bannerList = arrayListOf<BannerModel>()
                    value.documents.forEach {
                        val banner = it.toObject(BannerModel::class.java)
                        banner?.let { it1 -> bannerList.add(it1) }
                    }
                    _banners.value = Resource.Success(bannerList)
                }
            }
    }

}