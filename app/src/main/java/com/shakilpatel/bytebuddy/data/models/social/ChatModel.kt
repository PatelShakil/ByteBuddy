package com.shakilpatel.bytebuddy.data.models.social

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
