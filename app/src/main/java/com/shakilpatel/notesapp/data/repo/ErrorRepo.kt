package com.shakilpatel.notesapp.data.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.error.FAQAnsModel
import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.data.models.error.VoteModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.data.notification.Cons
import com.shakilpatel.notesapp.data.notification.NotificationData
import com.shakilpatel.notesapp.data.notification.PushNotification
import javax.inject.Inject

class ErrorRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val commonRepo: CommonRepo
) {
    fun onUpVote(ans: FAQAnsModel) {
        val ansRef = db.collection("faq").document(ans.faqId)

        ansRef.get()
            .addOnSuccessListener { faqSnapshot ->
                if (faqSnapshot.exists()) {
                    val faq = faqSnapshot.toObject(FAQModel::class.java)!!

                    val existingAns = faq.answers.find { it.id == ans.id }

                    if (existingAns != null) {
                        val voteList = existingAns.voteList.toMutableList()

                        val vote = voteList.find { it.userId == auth.uid.toString() }

                        if (vote != null) {
                            if (!vote.isUp) {
                                // User is switching from downvote to upvote
                                vote.isUp = true

                            } else {
                                // User has already upvoted, do nothing
                                voteList.remove(vote)
                                return@addOnSuccessListener
                            }
                        } else {
                            voteList.add(VoteModel(ans.id, true, auth.uid.toString()))
                        }

                        existingAns.voteList = voteList

                        // Create a new list of answers with the updated answer
                        val updatedAnswers = faq.answers.map {
                            if (it.id == existingAns.id) existingAns else it
                        }

                        // Update the FAQ model with the new list of answers
                        faq.answers = updatedAnswers

                        ansRef.set(faq)
                            .addOnSuccessListener {
                                Log.d("Done", true.toString())
                                    commonRepo.getUserNameFromUid(auth.uid.toString()) { name ->
                                        commonRepo.getTokenFromUid(ans.userId) { token ->
                                            com.shakilpatel.notesapp.data.notification.Cons.sendNotification(
                                                com.shakilpatel.notesapp.data.notification.PushNotification(
                                                    com.shakilpatel.notesapp.data.notification.NotificationData(
                                                        faq.title,
                                                        "$name has Upvoted👍 your Answer on #${faq.id}",
                                                        "",
                                                        "faq"
                                                    ),
                                                    token
                                                )
                                            )
                                        }

                                    }
                            }
                    }
                }
            }
    }

    fun onDownVote(ans: FAQAnsModel) {
        val ansRef = db.collection("faq").document(ans.faqId)

        ansRef.get()
            .addOnSuccessListener { faqSnapshot ->
                if (faqSnapshot.exists()) {
                    val faq = faqSnapshot.toObject(FAQModel::class.java)!!

                    val existingAns = faq.answers.find { it.id == ans.id }

                    if (existingAns != null) {
                        val voteList = existingAns.voteList.toMutableList()

                        val vote = voteList.find { it.userId == auth.uid.toString() }

                        if (vote != null) {
                            if (vote.isUp) {
                                // User is switching from upvote to downvote
                                vote.isUp = false


                            } else {
                                // User has already downvoted, do nothing
                                voteList.remove(vote)

                                return@addOnSuccessListener
                            }
                        } else {
                            voteList.add(VoteModel(ans.id, false, auth.uid.toString()))
                        }

                        existingAns.voteList = voteList

                        // Create a new list of answers with the updated answer
                        val updatedAnswers = faq.answers.map {
                            if (it.id == existingAns.id) existingAns else it
                        }

                        // Update the FAQ model with the new list of answers
                        faq.answers = updatedAnswers

                        ansRef.set(faq)
                            .addOnSuccessListener {
                                Log.d("Done", true.toString())
                                    commonRepo.getUserNameFromUid(auth.uid.toString()) { name ->
                                        commonRepo.getTokenFromUid(ans.userId) { token ->
                                            com.shakilpatel.notesapp.data.notification.Cons.sendNotification(
                                                com.shakilpatel.notesapp.data.notification.PushNotification(
                                                    com.shakilpatel.notesapp.data.notification.NotificationData(
                                                        faq.title,
                                                        "$name has Downvoted👎 your Answer on #${faq.id}",
                                                        "",
                                                        "faq"
                                                    ),
                                                    token
                                                )
                                            )
                                        }
                                }
                            }
                    }
                }
            }
    }




    fun uploadError(error: FAQModel, onErrorUploaded: (Resource<Boolean>) -> Unit) {
        onErrorUploaded(Resource.Loading)
        db.collection("faq")
            .document(error.id)
            .set(error)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onErrorUploaded(Resource.Success(true))
                    commonRepo.getUserNameFromUid(error.userId){

                        Cons.sendNotification(
                            PushNotification(
                                NotificationData(
                                    error.title,
                                    "$it has asked '${error.title}?' \n Did you know about it?",
                                    "",
                                    "faq"
                                ),
                                Cons.TOPIC_ALL
                            )
                        )
                    }
                }
                if (it.exception != null)
                    onErrorUploaded(
                        Resource.Failure(
                            it.exception!!,
                            it.exception!!.localizedMessage
                        )
                    )
            }
    }

    fun getAllErrorsList(onResult: (Resource<ArrayList<FAQModel>>) -> Unit) {
        onResult(Resource.Loading)
        db.collection("faq")
            .addSnapshotListener { value, error ->
                if (error != null)
                    onResult(Resource.Failure(error, error.localizedMessage))
                if (value != null) {
                    val list = ArrayList<FAQModel>()
                    if (!value.isEmpty) {
                        list.clear()
                        for (error in value.documents) {
                            list.add(error.toObject(FAQModel::class.java)!!)
                        }
                        onResult(Resource.Success(list))
                    }
                }
            }
    }

    fun saveError(id: String, userUid: String, onSaved: (Resource<Boolean>) -> Unit) {
// Fetch the user document
        val userDocumentRef = db.collection("users").document(userUid)

        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userModel = documentSnapshot.toObject(UserModel::class.java)
                if (userModel != null) {
                    // Extract the SavedModel object
                    val savedModel = userModel.saved

                    // Modify the errors list
                    val updatedErrorsList = savedModel.errors.toMutableList()

                    if (updatedErrorsList.contains(id)) {
                        // If the error exists, remove it from the list
                        updatedErrorsList.remove(id)
                    } else {
                        // If the error doesn't exist, add it to the list
                        updatedErrorsList.add(id)
                    }

                    savedModel.errors = updatedErrorsList

                    // Update the user document in Firestore
                    userModel.saved = savedModel
                    userDocumentRef.set(userModel)
                        .addOnSuccessListener {
                            // Successfully updated the errors list
                            onSaved(Resource.Success(true))
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                            onSaved(Resource.Failure(e, e.localizedMessage))
                        }
                }
            }
        }
    }

    fun uploadFAQAns(faqAns: FAQAnsModel, onResult: (Resource<Boolean>) -> Unit) {
        val faqRef = db.collection("faq")
            .document(faqAns.faqId)

        faqRef.get().addOnSuccessListener { value ->
            if (value != null) {
                if (value.exists()) {
                    val faqModel = value.toObject(FAQModel::class.java)!!
                    val faqAnsList = faqModel.answers.toMutableList()
                    faqAnsList.add(faqAns)
                    faqModel.answers = faqAnsList
                    faqRef.set(faqModel)
                        .addOnSuccessListener {
                            onResult(Resource.Success(true))
                            commonRepo.getUserNameFromUid(faqAns.userId){name ->
                                commonRepo.getTokenFromUid(faqModel.userId){token->
                                    Cons.sendNotification(
                                        PushNotification(
                                            NotificationData(
                                                "$name Answered your FAQ",
                                                "${faqAns.message}",
                                                "",
                                                "faq"
                                            ),
                                            token
                                        )
                                    )
                                }

                            }

                        }
                        .addOnFailureListener {
                            onResult(Resource.Failure(it, it.localizedMessage))
                        }

                }
            }
        }
    }


}