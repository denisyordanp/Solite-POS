package com.socialite.solite_pos.view.ui.extensions

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No instance provided")
}