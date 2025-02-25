package com.shakilpatel.bytebuddy.data.models.error

data class VoteModel(
    val ansId: String,
    var isUp: Boolean,
    val userId: String
) {
    constructor() : this("", false, "")
}