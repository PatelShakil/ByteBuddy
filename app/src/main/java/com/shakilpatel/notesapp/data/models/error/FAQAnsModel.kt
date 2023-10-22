package com.shakilpatel.notesapp.data.models.error

data class FAQAnsModel(
    val id: String,
    val faqId: String,
    val message: String,
    val code: String,
    val date: Long,
    val userId: String,
    var voteList: List<VoteModel>
) {
    constructor() : this("", "", "", "", 0, "", emptyList())
}
