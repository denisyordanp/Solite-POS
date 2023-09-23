package com.socialite.common.extension

fun Boolean.toResponse(): String {
    return if (this) "True" else "False"
}