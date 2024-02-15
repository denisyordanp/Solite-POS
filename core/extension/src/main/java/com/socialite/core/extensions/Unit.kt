package com.socialite.core.extensions

fun (() -> Unit)?.orNothing() = this ?: { }