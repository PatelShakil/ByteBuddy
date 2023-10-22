package com.shakilpatel.notesapp.data.models.user

data class SavedModel(
    var projects: List<String>,
    var notes: List<String>,
    var errors: List<String>
) {
    constructor() : this(emptyList(), emptyList(), emptyList())
}
