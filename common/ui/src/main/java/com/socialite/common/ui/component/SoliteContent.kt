package com.socialite.common.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.socialite.common.ui.R
import com.socialite.common.ui.extension.LocalAlertDialog
import com.socialite.common.ui.extension.LocalBottomSheet
import com.socialite.common.ui.extension.LocalCoroutineScope
import com.socialite.common.ui.extension.LocalNavController
import com.socialite.common.ui.extension.LocalSnackBar
import com.socialite.common.ui.state.AlertDialogState
import com.socialite.common.ui.state.BottomSheetState
import com.socialite.core.extensions.orEmpty
import com.socialite.core.extensions.orNothing
import com.socialite.core.ui.extension.bottomSheet
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.theme.SolitePOSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoliteContent(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (modifier: Modifier) -> Unit
) {
    SolitePOSTheme {
        val navController = rememberNavController()
        val snackBarHostState = remember { SnackbarHostState() }
        val alertDialogState = remember { AlertDialogState() }
        val bottomSheetState = remember { BottomSheetState() }
        val coroutineScope = rememberCoroutineScope()

        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalSnackBar provides snackBarHostState,
            LocalAlertDialog provides alertDialogState,
            LocalBottomSheet provides bottomSheetState,
            LocalCoroutineScope provides coroutineScope
        ) {
            ModalBottomSheetLayout(
                sheetContent = {
                    BaseBottomSheetContent(
                        onDismiss = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        },
                        content = bottomSheetState.content
                    )
                },
                sheetState = bottomSheetState.state,
                content = {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                        topBar = topBar,
                        bottomBar = bottomBar,
                        backgroundColor = MaterialTheme.colors.background
                    ) { padding ->
                        content(Modifier.padding(padding))
                    }
                },
                sheetShape = MaterialTheme.shapes.bottomSheet,
                sheetBackgroundColor = MaterialTheme.colors.background
            )

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

@Composable
private fun BaseBottomSheetContent(
    onDismiss: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onDismiss,
            content = {
                Icon(
                    modifier = Modifier.padding(MaterialTheme.paddings.smallMedium),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null
                )
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.medium))
        content?.invoke(this)
    }
}