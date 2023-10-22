package com.shakilpatel.notesapp.data.models.learning

import java.io.Serializable

data class SemModel(
    val id:String,
    val name:String,
    val courseId:String,
    val subjects:List<SubjectModel>,
){
    constructor():this("","","", emptyList())
}
