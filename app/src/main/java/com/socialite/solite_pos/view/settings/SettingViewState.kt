package com.socialite.solite_pos.view.settings

data class SettingViewState(
    val isDarkMode: Boolean,
    val isLoading: Boolean,
    val isServerActive: Boolean,
    val error: Throwable?
) {
    companion object {
        fun idle() = SettingViewState(
            isDarkMode = false,
            isLoading = false,
            isServerActive = false,
            error = null
        )
    }
}