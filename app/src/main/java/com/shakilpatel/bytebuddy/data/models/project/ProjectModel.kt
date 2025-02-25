package com.shakilpatel.bytebuddy.data.models.project

import com.shakilpatel.bytebuddy.data.models.learning.PLModel
import com.shakilpatel.bytebuddy.data.models.social.CommentModel
import com.shakilpatel.bytebuddy.data.models.user.UserModel

data class ProjectModel(
    val id:String,
    val views:List<UserModel>,
    val title:String,
    val platform:String,
    val thumbnail:String,
    val date:Long,
    val pl:List<PLModel>,
    val images:List<String>,
    val discussion:List<CommentModel>
){
    constructor():this("", emptyList(),"","","",0, emptyList(), emptyList(), emptyList())
}
