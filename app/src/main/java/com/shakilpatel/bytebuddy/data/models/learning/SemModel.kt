package com.shakilpatel.bytebuddy.data.models.learning

data class SemModel(
    val id:String,
    val name:String,
    val courseId:String,
    val subjects:List<SubjectModel>,
){
    constructor():this("","","", emptyList())
}
