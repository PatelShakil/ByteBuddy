package com.shakilpatel.bytebuddy.data.models.learning

data class NotesPage(
    val pageNo : Int,
    var text : String
){
    constructor():this(0,"")
}
