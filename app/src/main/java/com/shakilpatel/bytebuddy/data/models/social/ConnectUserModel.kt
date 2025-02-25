package com.shakilpatel.bytebuddy.data.models.social

data class ConnectUserModel(
    val senderId : String,
    val receiverId : String,
    val lastMsg : String,
    val lastMsgTime : Long,
){
    constructor():this("","","",System.currentTimeMillis())
}
