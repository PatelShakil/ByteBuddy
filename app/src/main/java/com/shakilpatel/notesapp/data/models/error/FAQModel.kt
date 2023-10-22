package com.shakilpatel.notesapp.data.models.error

data class FAQModel(
    val id: String,
    val title: String,
    val description: String,
    val userId: String,
    val code: String,
    val date: Long,
    var answers: List<FAQAnsModel>,
    val views: List<String>,
    val screenshots: List<String>,
) {
    constructor() : this(
        "", "", "", "", "", 0, emptyList(), emptyList(), emptyList()
    )
}
