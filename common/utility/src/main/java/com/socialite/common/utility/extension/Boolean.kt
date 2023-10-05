package com.socialite.common.utility.extension

fun Boolean.toResponse(): String {
    return if (this) "True" else "False"
}