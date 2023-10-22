package com.shakilpatel.notesapp.data.models.user

import com.shakilpatel.notesapp.data.models.learning.CollegeModel
import com.shakilpatel.notesapp.data.models.learning.EducationModel

data class UserModel(
    var uid: String,
    var name: String,
    val email: String,
    var dob: String,
    var gender: String,
    val faculty: Boolean = false,
    var online: Boolean = false,
    var profileImg: String?,
    var education: EducationModel,
    var college: CollegeModel,
    val date: Long,
    var token:String,
    var lastSeen:Long,
    var saved: SavedModel,
    var notifications:List<NotificationModel>
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        false,
        false,
        null,
        EducationModel(),
        CollegeModel(),
        0,
        "",
        0,
        SavedModel(),
        emptyList()
    )
}