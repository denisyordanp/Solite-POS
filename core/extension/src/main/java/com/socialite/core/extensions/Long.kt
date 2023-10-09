package com.socialite.core.extensions

fun Long.toIDR() = "IDR ${this.thousand().rupiahToK()}"
fun Long.thousand(): String = String.format("%,d", this)