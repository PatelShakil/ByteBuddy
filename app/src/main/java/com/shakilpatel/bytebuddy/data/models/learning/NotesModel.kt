package com.shakilpatel.bytebuddy.data.models.learning

data class NotesModel(
    val id: String,
    val subjectId: String,
    val courseId: String,
    val title: String,
    val description: String,
    var pdfFile: String,
    val author: String,
    val verified: Boolean,
    var text: List<NotesPage>,
    var views: List<String>,
    val date: Long,
) {
    constructor() : this("", "", "", "", "", "", "", false, emptyList(), emptyList(), 0)
}
