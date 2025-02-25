package com.shakilpatel.bytebuddy.data.notification

import com.shakilpatel.bytebuddy.data.notification.Cons.CONTENT_TYPE
import com.shakilpatel.bytebuddy.data.notification.Cons.SERVER_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers(*["Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE"])
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification?): Call<PushNotification?>?

}