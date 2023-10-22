package com.shakilpatel.notesapp.data.notification

import android.util.Log
import com.shakilpatel.notesapp.data.notification.ApiUtils

object Cons {
    const val BASE_URL = "https://fcm.googleapis.com/"

    //    const val SERVER_KEY =
//        "AAAAmbXktVQ:APA91bHohc5IUw1TlRJzvtx5NFmNgwqFji3vDFV9jmu7FPh_MKOHQtqDDHc4DaXCpUcH-S0z3D6UaAxo0OpWIY2qRYia8mP6O7qqjFnlcYZMTilHeX7U7D8rkCRZSFiNXgwlnG2SJZwR"
    const val SERVER_KEY = "AAAAqNEapiQ:APA91bEvI8y6aaxdLZ1NTA1jX2MH-w2rY6aLmpcjrar8XP1nLYcBmsAITOHaMY3dk-mkzsBiSD5zEs2N5v3whyTFGwLPME05md_QBYjUEsW5xxqpM3zmZ4A2011jjsJo9EAbdAteAVy4"
    const val CONTENT_TYPE = "application/json"
    const val TOPIC_ALL = "/topics/all"

    fun sendNotification(notification: PushNotification) {
        ApiUtils.client.sendNotification(notification)
            ?.enqueue(object : retrofit2.Callback<PushNotification?> {
                override fun onResponse(
                    call: retrofit2.Call<PushNotification?>,
                    response: retrofit2.Response<PushNotification?>
                ) {
                    Log.d("Success Notification", response.toString())
                    //handle on successfully sending notification
                }

                override fun onFailure(call: retrofit2.Call<PushNotification?>, t: Throwable) {
                    //handle on sending noti
                    // notification failure
                    Log.d("Notification Error", t.localizedMessage)
                }
            })
    }


    //Manifest permissions and services


}