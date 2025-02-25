package com.shakilpatel.bytebuddy.common

sealed class Resource<out R> {
    data class Success<out R>(val result: R):Resource<R>()
    data class Failure(
        val ex : Exception,
        val errorMsgBody:String
    ): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}