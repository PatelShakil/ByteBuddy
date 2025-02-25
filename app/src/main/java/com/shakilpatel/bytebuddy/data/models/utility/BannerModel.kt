package com.shakilpatel.bytebuddy.data.models.utility

data class BannerModel(
    val position : Int,
    val image : String,
    val url : String,
    val is_active : Boolean,
){
    constructor():this(1,"","",false)
}
