package com.socialite.core.extensions

fun Boolean.toResponse(): String {
    return if (this) "True" else "False"
}