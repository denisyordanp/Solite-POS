package com.socialite.common.ui.extension

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.socialite.common.ui.state.AlertDialogState
import com.socialite.common.ui.state.BottomSheetState
import kotlinx.coroutines.CoroutineScope

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}

val LocalSnackBar = compositionLocalOf<SnackbarHostState> {
    error("No LocalSnackBar provided")
}

val LocalAlertDialog = compositionLocalOf<AlertDialogState> {
    error("No LocalAlertDialog provided")
}

val LocalBottomSheet = compositionLocalOf<BottomSheetState> {
    error("No LocalBottomSheet provided")
}

val LocalCoroutineScope = compositionLocalOf<CoroutineScope> {
    error("No LocalCoroutineScope provided")
}