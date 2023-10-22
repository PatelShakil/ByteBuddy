package com.shakilpatel.notesapp.data.models.user

import com.shakilpatel.notesapp.data.models.error.FAQModel
import com.shakilpatel.notesapp.data.models.learning.NotesModel
import com.shakilpatel.notesapp.data.models.project.ProjectModel

data class SavedModelExp(
    var notes: ArrayList<NotesModel>,
    var errors: ArrayList<FAQModel>,
    val projects: ArrayList<ProjectModel>
) {
    constructor() : this(ArrayList(), ArrayList(), ArrayList())
}
