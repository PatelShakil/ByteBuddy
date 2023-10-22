package com.shakilpatel.notesapp.data.models.social

import com.shakilpatel.notesapp.data.models.user.UserModel

data class ReplyModel(
    val id:String,
    val commentId:String,
    val date:Long,
    val user:UserModel,
    val message:String
){
    constructor():this("","",0,UserModel(),"")
}
