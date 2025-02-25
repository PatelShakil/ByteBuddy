package com.shakilpatel.bytebuddy.data.models.user

data class NotificationModel(
    val id:String,
    val title:String,
    val message:String,
    val faq:String,
    val notesId:String,
    var read:Boolean,
    val date : Long
){
    constructor():this(
        "",
        "",
        "",
        "",
        "",
        false,
        0
    )
}
