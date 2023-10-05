package com.socialite.core.extensions

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString?.orEmpty() = if (this.isNullOrEmpty()) AnnotatedString("") else this