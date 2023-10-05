package com.socialite.core.extensions

fun String?.toLongDefault(default: Long) = if (this.isNullOrEmpty()) default else this.toLong()