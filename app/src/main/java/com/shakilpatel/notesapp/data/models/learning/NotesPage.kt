package com.shakilpatel.notesapp.data.models.learning

data class NotesPage(
    val pageNo : Int,
    var text : String
){
    constructor():this(0,"")
}
