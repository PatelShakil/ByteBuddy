package com.shakilpatel.bytebuddy.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import javax.inject.Inject

class ProjectRepo @Inject constructor(private val db : FirebaseFirestore) {
    fun saveProject(projectID:String,userUid:String){
        // Fetch the user document
        val userDocumentRef = db.collection("users").document(userUid)

        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userModel = documentSnapshot.toObject(UserModel::class.java)
                if (userModel != null) {
                    // Extract the SavedModel object
                    val savedModel = userModel.saved

                    // Modify the projects list
                    val updatedProjectsList = savedModel.projects.toMutableList()

                    if (updatedProjectsList.contains(projectID)) {
                        // If the project exists, remove it from the list
                        updatedProjectsList.remove(projectID)
                    } else {
                        // If the project doesn't exist, add it to the list
                        updatedProjectsList.add(projectID)
                    }

                    savedModel.projects = updatedProjectsList

                    // Update the user document in Firestore
                    userModel.saved = savedModel
                    userDocumentRef.set(userModel)
                        .addOnSuccessListener {
                            // Successfully updated the projects list
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                }
            }
        }
    }
}