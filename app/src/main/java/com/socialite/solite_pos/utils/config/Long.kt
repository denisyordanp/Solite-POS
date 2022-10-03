package com.socialite.solite_pos.utils.config

import java.text.NumberFormat
import java.util.Locale

fun Long.toIDR() = "IDR ${this.thousand().rupiahToK()}"

fun Long.thousand(): String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
