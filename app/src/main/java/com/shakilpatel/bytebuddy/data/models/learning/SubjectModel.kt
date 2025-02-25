package com.shakilpatel.bytebuddy.data.models.learning

data class SubjectModel(
    val id:String,
    val subjectName:String,
    val date:Long,
    val courseId:String,
    val notesList:List<NotesModel>,
){
    constructor():this("","",0,"", emptyList())
}