package com.shakilpatel.bytebuddy.data.models.learning

data class CourseModel(
    val id:String,
    val name: String,
    val semList:List<SemModel>,
    val universityName:String,
    val description:String
){
    constructor():this("","", emptyList(),"","")
}
