package com.shakilpatel.bytebuddy.data.models.user

import com.shakilpatel.bytebuddy.data.models.error.FAQModel
import com.shakilpatel.bytebuddy.data.models.learning.NotesModel
import com.shakilpatel.bytebuddy.data.models.project.ProjectModel

data class SavedModelExp(
    var notes: ArrayList<NotesModel>,
    var errors: ArrayList<FAQModel>,
    val projects: ArrayList<ProjectModel>
) {
    constructor() : this(ArrayList(), ArrayList(), ArrayList())
}
