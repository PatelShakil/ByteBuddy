package com.shakilpatel.bytebuddy.ui.main.home.notes

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.translator.EasyTranslator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.learning.CourseModel
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.learning.NotesPage
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import com.shakilpatel.bytebuddy.data.repo.CommonRepo
import com.shakilpatel.bytebuddy.data.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepo: NotesRepo,
    private val commonRepo: CommonRepo,
    private val context: Context,
    private val auth: FirebaseAuth,
    private val db : FirebaseFirestore
) : ViewModel() {




    private val _course = MutableStateFlow("")
    val course = _course.asStateFlow()

    fun setCourse(value: String) {
        _course.value = value
    }

    private val _subject = MutableStateFlow("")
    var subject = _subject.asStateFlow()

    private val _savedNotes = MutableStateFlow<Resource<List<NotesModel>>?>(null)
    val savedNotes = _savedNotes.asStateFlow()

    fun getSavedNotes(uid: String = auth.uid.toString()) = viewModelScope.launch {
        _savedNotes.value = Resource.Loading
        try {
            val userSnapshot = db.collection("users").document(uid).get().await()
            val list = userSnapshot.toObject(UserModel::class.java)?.saved?.notes
            val notesList = mutableListOf<NotesModel>()
            if (!list.isNullOrEmpty()) {
                list.forEach { noteId ->
                    val noteSnapshot = db.collection("notes").document(noteId).get().await()
                    noteSnapshot.toObject(NotesModel::class.java)?.let { notesList.add(it) }
                }
                _savedNotes.value = Resource.Success(notesList)
            } else {
                _savedNotes.value = Resource.Success(emptyList())  // Handle empty notes
            }
        } catch (e: Exception) {
            _savedNotes.value = Resource.Failure(e,e.message ?: "Unknown Error")
        }
    }


    fun setSubject(value: String) {
        _subject.value = value
    }



    var notesId = MutableStateFlow<String>("")
    var curNote = mutableStateOf(NotesModel())

    val translator = EasyTranslator(context)
    val isProgressRun = mutableStateOf(false)
    var textToSpeech: TextToSpeech? = null
    val isReadAloudPlaying = textToSpeech?.isSpeaking
    fun getTranslatedText(
        notes: NotesPage,
        langCode: String,
        onSuccess: (NotesPage) -> Unit,
        onError: (String) -> Unit
    ) {
        isProgressRun.value = true
        notesRepo.getPageTranslation(translator, notes, langCode, {
            onSuccess(it)
            isProgressRun.value = false
        }) {
            onError(it)
            isProgressRun.value = false
        }
    }

    fun saveNotes(id: String, uid: String = FirebaseAuth.getInstance().uid.toString()) =
        viewModelScope.launch {
            notesRepo.saveNotes(id, uid)
        }

    fun getUserModel(uid: String = auth.uid.toString(), getUser: (UserModel) -> Unit) {
        commonRepo.getUserModel(uid) {
            getUser(it)
        }
    }

    init {
        getCoursesList()
        initializeTextToSpeech()

    }


    fun registerView(id: String) {
        notesRepo.registerView(id) {
            //result
        }
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.getDefault()
            }
        }
    }

    fun speak(text: String) {
        terminate()
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun terminate() {
        textToSpeech?.stop()
    }

    override fun onCleared() {
        textToSpeech?.shutdown()
        super.onCleared()
    }

    private val _courses = MutableStateFlow<Resource<List<CourseModel>>?>(null)
    val courses: MutableStateFlow<Resource<List<CourseModel>>?>
        get() = _courses

    private fun getCoursesList() = viewModelScope.launch {
        notesRepo.getCoursesList {
            _courses.value = it
            Log.d("TAG", "getCoursesList: ${_courses.value}")
        }
    }


    var notes = MutableStateFlow<Resource<NotesModel>?>(null)
    fun getNote(notesId: String) = viewModelScope.launch {
        notes.value = Resource.Loading
        db.collection("notes")
            .document(notesId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    if (value.exists()) {
                        notes.value = Resource.Success(value.toObject(NotesModel::class.java)!!)
                    }
                }
            }
    }

    private val _notesCol = MutableStateFlow<Resource<List<NotesModel>>?>(null)
    val notesCol: MutableStateFlow<Resource<List<NotesModel>>?>
        get() = _notesCol

    fun getNotesCol(subject: String, course: String = "") = viewModelScope.launch {
//        _notesCol.value = Resource.Loading
        notesRepo.getNotesCol(subject, course) {
            _notesCol.value = it
        }
    }

}