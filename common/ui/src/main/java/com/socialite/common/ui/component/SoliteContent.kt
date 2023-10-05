package com.socialite.common.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.socialite.common.ui.extension.LocalAlertDialog
import com.socialite.common.ui.extension.LocalNavController
import com.socialite.common.ui.extension.LocalSnackBar
import com.socialite.common.ui.state.AlertDialogState
import com.socialite.core.extensions.orEmpty
import com.socialite.core.extensions.orNothing
import com.socialite.core.ui.theme.SolitePOSTheme

@Composable
fun SoliteContent(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (modifier: Modifier) -> Unit
) {
    SolitePOSTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        val alertDialogState = remember { AlertDialogState() }

        CompositionLocalProvider(
            LocalNavController provides rememberNavController(),
            LocalSnackBar provides snackBarHostState,
            LocalAlertDialog provides alertDialogState
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                topBar = topBar,
                bottomBar = bottomBar
            ) { padding ->
                content(Modifier.padding(padding))
            }

            if (alertDialogState.shouldShowDialog) {
                SoliteAlertDialog(
                    titleText = alertDialogState.title.orEmpty(),
                    descText = alertDialogState.message.orEmpty(),
                    descAnnotatedString = alertDialogState.messageAnnotated.orEmpty(),
                    positiveAction = alertDialogState.buttonPositiveAction.orNothing(),
                    positiveText = alertDialogState.buttonPositiveText.orEmpty(),
                    negativeAction = alertDialogState.buttonNegativeAction,
                    negativeText = alertDialogState.buttonNegativeText.orEmpty(),
                    onDismiss = alertDialogState::dismissed
                )
            }
        }
    }
}