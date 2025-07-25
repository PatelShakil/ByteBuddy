package com.shakilpatel.bytebuddy.data.models.social

import com.shakilpatel.bytebuddy.data.models.user.UserModel

data class CommentModel(
    val id:String,
    val message:String,
    val date:Long,
    val user:UserModel,
    val likes:List<UserModel>,
    val replies:List<ReplyModel>
){
    constructor():this("","",0,UserModel(), emptyList(), emptyList())
}
