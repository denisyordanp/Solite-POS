package com.socialite.common.ui.state

import androidx.compose.ui.text.AnnotatedString

class AlertDialogState {
    var title: String? = null
        private set
    var message: String? = null
        private set
    var messageAnnotated: AnnotatedString? = null
        private set

    var buttonPositiveText: String? = null
        private set
    var buttonNegativeText: String? = null
        private set

    var buttonPositiveAction: (() -> Unit)? = null
        private set
    var buttonNegativeAction: (() -> Unit)? = null
        private set

    var shouldShowDialog: Boolean = false
        private set

    fun show(
        title: String,
        message: String,
        buttonPositiveText: String,
        buttonPositiveAction: (() -> Unit),
        buttonNegativeText: String? = null,
        buttonNegativeAction: (() -> Unit)? = null,
    ) {
        this.title = title
        this.message = message
        this.buttonPositiveText = buttonPositiveText
        this.buttonPositiveAction = buttonPositiveAction
        this.buttonNegativeText = buttonNegativeText
        this.buttonNegativeAction = buttonNegativeAction

        shouldShowDialog = true
    }

    fun show(
        title: String,
        message: AnnotatedString,
        buttonPositiveText: String,
        buttonPositiveAction: (() -> Unit),
        buttonNegativeText: String? = null,
        buttonNegativeAction: (() -> Unit)? = null,
    ) {
        this.title = title
        this.messageAnnotated = message
        this.buttonPositiveText = buttonPositiveText
        this.buttonPositiveAction = buttonPositiveAction
        this.buttonNegativeText = buttonNegativeText
        this.buttonNegativeAction = buttonNegativeAction

        shouldShowDialog = true
    }

    fun dismissed() {
        this.title = null
        this.message = null
        this.buttonPositiveText = null
        this.buttonPositiveAction = null
        this.buttonNegativeText = null
        this.buttonNegativeAction = null

        shouldShowDialog = false
    }
}