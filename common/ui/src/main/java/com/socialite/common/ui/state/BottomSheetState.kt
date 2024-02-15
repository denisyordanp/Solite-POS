package com.socialite.common.ui.state

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
class BottomSheetState {
    var content: @Composable (ColumnScope.() -> Unit)? = null
        private set

    val state: ModalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)

    suspend fun show(
        content: @Composable ColumnScope.() -> Unit
    ) {
        this.content = content

        state.animateTo(ModalBottomSheetValue.Expanded)
    }

    suspend fun hide() {
        state.hide()
        this.content = null
    }
}