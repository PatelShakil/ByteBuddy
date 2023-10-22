package com.shakilpatel.notesapp.data.repo

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.easy.translator.EasyTranslator
import com.easy.translator.LanguagesModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.learning.CourseModel
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.learning.NotesPage
import com.shakilpatel.notesapp.data.models.learning.SemModel
import com.shakilpatel.notesapp.data.models.learning.SubjectModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.data.notification.Cons
import com.shakilpatel.notesapp.data.notification.Cons.TOPIC_ALL
import com.shakilpatel.notesapp.data.notification.NotificationData
import com.shakilpatel.notesapp.data.notification.PushNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class NotesRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val database: FirebaseDatabase,
    private val commonRepo: CommonRepo
) {

    fun registerView(id: String, onResult: (Resource<Boolean>) -> Unit) {
        val notesRef = db.collection("notes")
            .document(id)
        notesRef.get().addOnSuccessListener {
            if (it.exists()) {
                val notes = it.toObject(NotesModel::class.java)!!
                val list = notes.views.toMutableList()
                if (!list.contains(auth.uid.toString())) {
                    list.add(auth.uid.toString())
                    notes.views = list

                    notesRef.set(notes).addOnSuccessListener {
                        onResult(Resource.Success(true))
                    }
                        .addOnFailureListener {
                            onResult(Resource.Failure(it, it.localizedMessage))
                        }
                }

            }
        }
    }


    suspend fun getTranslatedNotes(
        context: Context,
        list: List<NotesPage>,
        langCode: String,
        onSuccess: (List<NotesPage>) -> Unit,
        onError: (String) -> Unit
    ) {

        val upList = mutableListOf<NotesPage>()
        try {
            val t = Thread()
            t.run {
                list.forEach {
//                    getPageTranslation(translator,it,langCode){
//                        upList.add(it)
//                        onSuccess(upList)
//                    }
                }
            }
            t.start()

        } catch (e: Exception) {
            onError(e.message ?: "An error occurred")
        }
    }

    fun getPageTranslation(
        translator: EasyTranslator,
        notes: NotesPage,
        langCode: String,
        onSuccess: (NotesPage) -> Unit,
        onError: (String) -> Unit
    ) {
        translator.translate(
            notes.text,
            LanguagesModel.AUTO_DETECT,
            translator.getLanguagesList().find { it.shortCode == langCode }!!,
            {
                onSuccess(NotesPage(notes.pageNo, it))
            }, {
                onError(it)
            },
            20000
        )
    }

    fun getCoursesList(onResult: (Resource<ArrayList<CourseModel>>) -> Unit) {
        database.reference.child("courses")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val courses = ArrayList<CourseModel>()
                    if (snapshot.exists()) {
                        for (course in snapshot.children) {
                            val semList = ArrayList<SemModel>()
                            for (sem in course.children) {
                                val subjects = ArrayList<SubjectModel>()
                                for (subject in sem.children) {
                                    subjects.add(
                                        SubjectModel(
                                            subject.key.toString(),
                                            subject.value.toString(),
                                            0,
                                            course.key.toString(),
                                            emptyList()
                                        )
                                    )
                                }
                                semList.add(
                                    SemModel(
                                        sem.key.toString(),
                                        sem.key.toString(),
                                        course.key.toString(),
                                        subjects
                                    )
                                )
                            }
                            courses.add(
                                CourseModel(
                                    "",
                                    course.key.toString(),
                                    semList,
                                    "VNSGU",
                                    ""
                                )
                            )
                        }
                        onResult(Resource.Success(courses))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(Resource.Failure(error.toException(), error.message.toString()))
                }

            })
    }

    suspend fun getTextFromPDF(context: Context, pdfFile: String): List<NotesPage> {
        //get text from pdf file
        try {
            var page = NotesPage()
            val list = mutableListOf<NotesPage>()
            withContext(Dispatchers.IO) {
                // Convert content URI to a file path
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(Uri.parse(pdfFile))
                val cacheFile = File(context.cacheDir, "temp.pdf")
                inputStream?.use { input ->
                    cacheFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val reader = PdfReader(cacheFile.absolutePath)
                val n: Int = reader.numberOfPages
                for (i in 0 until n) {
                    page = NotesPage(
                        i + 1,
                        """${PdfTextExtractor.getTextFromPage(reader, i + 1).trim { it <= ' ' }}
            
            """.trimIndent()
                    ) //Extracting the content from the different pages
                    list.add(page)
                }

                Log.d("PDFFILE", page.toString())
                reader.close()
            }
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    suspend fun uploadNotes(
        context: Context,
        notes: NotesModel,
        onResult: (Resource<Boolean>) -> Unit
    ) {
        notes.text = getTextFromPDF(context, notes.pdfFile)
        val pdfRef = storage.reference.child("notes/${notes.title + notes.id}")
        val uploadTask = pdfRef.putFile(notes.pdfFile.toUri())

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    onResult(Resource.Failure(it, it.localizedMessage))
                }
            }
            return@Continuation pdfRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                notes.pdfFile = task.result.toString()
                db.collection("notes")
                    .document(notes.id)
                    .set(notes)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onResult(Resource.Success(true))
                            commonRepo.getUserNameFromUid(notes.author){

                                Cons.sendNotification(
                                    PushNotification(
                                        NotificationData(
                                            notes.courseId,
                                            "Uploaded notes for ${notes.subjectId} by $it",
                                            notes.id,
                                            ""
                                        ),
                                        TOPIC_ALL
                                    )
                                )
                            }
                        }
                        if (it.isCanceled) {
                            onResult(
                                Resource.Failure(
                                    it.exception!!,
                                    it.exception!!.localizedMessage
                                )
                            )
                        }
                    }
            } else {
                task.exception?.let {
                    onResult(Resource.Failure(it, it.localizedMessage))
                }
            }
        }
    }

    fun getNotesCol(
        subjectId: String,
        courseId: String,
        onResult: (Resource<ArrayList<NotesModel>>) -> Unit
    ) {
        getColRef(subjectId, courseId).addSnapshotListener { value, error ->
            val list = ArrayList<NotesModel>()
            if (error != null) {
                onResult(Resource.Failure(error, error.localizedMessage))
                return@addSnapshotListener
            }
            if (value != null) {
                if (!value.isEmpty) {
                    list.clear()
                    for (note in value.documents) {
                        list.add(note.toObject(NotesModel::class.java)!!)
                    }
                    onResult(Resource.Success(list))
                }
            } else {
                onResult(
                    Resource.Failure(
                        java.lang.Exception("No Data Found"),
                        "No Such Notes available at this time."
                    )
                )
            }
        }
    }

    private fun getColRef(subjectId: String, courseId: String): Query {
        return if (subjectId == "" && courseId == "") {
            db.collection("notes")
        } else if (subjectId != "" && courseId == "") {
            db.collection("notes")
                .whereEqualTo("subjectId", subjectId)
        } else if (subjectId == "" && courseId != "") {
            db.collection("notes")
                .whereEqualTo("courseId", courseId)
        } else if (subjectId != "" && courseId != "") {
            db.collection("notes")
                .whereEqualTo("subjectId", subjectId)
                .whereEqualTo("courseId", courseId)
        } else {
            db.collection("notes")
        }
    }

    fun saveNotes(noteID: String, userUid: String) {
        // Fetch the user document
        val userDocumentRef = db.collection("users").document(userUid)

        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userModel = documentSnapshot.toObject(UserModel::class.java)
                if (userModel != null) {
                    // Extract the SavedModel object
                    val savedModel = userModel.saved

                    // Modify the notes list
                    val updatedNotesList = savedModel.notes.toMutableList()

                    if (updatedNotesList.contains(noteID)) {
                        // If the note exists, remove it from the list
                        updatedNotesList.remove(noteID)
                    } else {
                        // If the note doesn't exist, add it to the list
                        updatedNotesList.add(noteID)
                    }

                    savedModel.notes = updatedNotesList

                    // Update the user document in Firestore
                    userModel.saved = savedModel
                    userDocumentRef.set(userModel)
                        .addOnSuccessListener {
                            // Successfully updated the notes list
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                }
            }
        }
    }
}