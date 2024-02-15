package com.shakilpatel.notesapp.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shakilpatel.notesapp.MainActivity
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.data.models.user.NotificationModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import java.util.*

class FirebaseService : FirebaseMessagingService() {
    var CHANNEL_ID = "channel_id"
    var MSG_CHANNEL_ID = "l_id"
    val userRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().uid.toString())


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        userRef.get().addOnSuccessListener { 
            if(it.exists()){
                val user = it.toObject(UserModel::class.java)!!
                user.token = token
                userRef.set(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "New Token Registered Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Msg", message.data.toString())
        if(message.data["ismsg"].toString() == "false") {
            val user = mutableStateOf(UserModel())
            if (message.data["title"] != null || message.data["title"] != "") {
                userRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        user.value = it.toObject(UserModel::class.java)!!
                        val notiList = user.value.notifications.toMutableList()
                        notiList.add(
                            NotificationModel(
                                Cons.generateRandomValue(9),
                                message.data["title"] ?: "".trim(),
                                message.data["message"] ?: "".trim(),
                                message.data["faq"] ?: "",
                                message.data["notesId"] ?: "",
                                false,
                                System.currentTimeMillis()
                            )
                        )
                        user.value.notifications = notiList
                        userRef.set(user.value)
                            .addOnSuccessListener {
                                Log.d("Notification", "Stored Successfully")
                            }
                    }
                }
            }
        }
        val manager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager)
        }

//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bytebuddy://example.com/notification"))
        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivities(
                this,
                0,
                arrayOf<Intent>(intent),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivities(
                this,
                0,
                arrayOf<Intent>(intent),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = if(message.data["ismsg"].toString() == "false") {
                Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(message.data["title"])
                    .setContentText(message.data["message"])
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setAutoCancel(true)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            applicationContext.resources,
                            R.drawable.logo
                        )
                    )
                    .setContentIntent(pendingIntent)
                    .build()
            }else{
                Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle(message.data["title"] + " sent you a message")
                    .setContentText(message.data["message"])
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setAutoCancel(true)
                    .setLargeIcon(
                        if(message.data["image"]?.toString() == "") {BitmapFactory.decodeResource(
                            applicationContext.resources,
                            R.drawable.ic_profile_round_img
                        )} else Cons.decodeImage(message.data["image"].toString())
                    )
                    .setContentIntent(pendingIntent)
                    .build()
            }
        }
        manager.notify(notificationId,notification)
        checkUserIsOnline(FirebaseAuth.getInstance().uid.toString()){
            if(!it){
                manager.notify(notificationId, notification)
            }
        }
    }
    fun checkUserIsOnline(uid: String, onResult: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance().collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    if (value.exists()) {
                        val user = value.toObject(UserModel::class.java)!!
                        onResult(user.online)
                    }
                }
            }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID, "ByteBuddy",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.setDescription("We notified you about each and every moment.")
        channel.enableLights(true)
        channel.setLightColor(Color.GREEN)
        channel.enableVibration(true)
        manager.createNotificationChannel(channel)
    }
}