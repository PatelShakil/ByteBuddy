package com.shakilpatel.notesapp.data.models.social

import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.project.ProjectModel

data class ChatModel(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val date: Long,
    val projectId: String,
    val notesId: String,
    val faqId: String
) {
    constructor() : this("", "", "", "", 0, "", "", "")
}
