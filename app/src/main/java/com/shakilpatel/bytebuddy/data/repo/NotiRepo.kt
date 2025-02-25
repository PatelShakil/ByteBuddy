package com.shakilpatel.bytebuddy.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.user.NotificationModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel
import javax.inject.Inject

class NotiRepo @Inject constructor(
    private val commonRepo: CommonRepo,
    private val db : FirebaseFirestore
) {

    fun getAllNotifications(uid : String,onResult : (Resource<List<NotificationModel>>) ->Unit){
        onResult(Resource.Loading)
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if(error != null){
                    onResult(Resource.Failure(error,error.localizedMessage))
                    return@addSnapshotListener
                }
                if(value != null){
                    if(value.exists()){
                        val user = value.toObject(UserModel::class.java)!!
                        if(user.notifications.isNotEmpty())
                            onResult(Resource.Success(user.notifications))
                        else
                            onResult(Resource.Failure(Exception("No Notifications Available"),"oops☹... no notifications available at this time"))
                    }else{
                        onResult(Resource.Failure(Exception("No Notifications Available"),"oops☹... no notifications available at this time"))
                    }
                }
            }
    }

    fun markReadNotification(id:String,uid:String){
        val userRef = db.collection("users")
            .document(uid)


        userRef.get().addOnSuccessListener {
            if(it.exists()){
                val user = it.toObject(UserModel::class.java)!!

                val notiList = user.notifications.toMutableList()

                val notification = notiList.find { it.id == id }

                if(notification != null){
                    notification.read = true
                    notiList.remove(notification)
                    notiList.add(notification)
                }



                user.notifications = notiList

                userRef.set(user).addOnSuccessListener {
                    Log.d("Notification","Read Successfully")
                }
            }
        }
    }




}