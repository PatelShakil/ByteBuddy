package com.shakilpatel.notesapp.data.notification

data class NotificationData(
    var title: String,
    var message: String,
    var notesId: String = "",
    var faq: String = "",
    var time: Long = System.currentTimeMillis(),
    var image: String = "",
    var uid: String = "",
)
