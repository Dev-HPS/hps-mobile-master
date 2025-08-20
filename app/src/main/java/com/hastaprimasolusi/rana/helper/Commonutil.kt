package com.hastaprimasolusi.rana.helper

object Helpers {
    fun <T> isEmptyOrNull(it: T?): Boolean {
        return when (it) {
            is String -> it.isEmpty() || it == "null" || it == " " || it == ""
            is Collection<*> -> it.isEmpty()
            is Array<*> -> it.contentEquals(emptyArray())
            else -> true
        }
    }
}