package com.socialite.common.utility.extension

fun String?.toLongDefault(default: Long) = if (this.isNullOrEmpty()) default else this.toLong()