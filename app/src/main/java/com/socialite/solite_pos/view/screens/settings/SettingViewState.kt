package com.socialite.solite_pos.view.screens.settings

import com.socialite.common.utility.state.ErrorState
import com.socialite.solite_pos.schema.GeneralMenuBadge


data class SettingViewState(
    val isDarkMode: Boolean,
    val isLoading: Boolean,
    val isServerActive: Boolean,
    val isSynchronizeSuccess: Boolean,
    val badges: List<GeneralMenuBadge>,
    val error: ErrorState?
) {
    companion object {
        fun idle() = SettingViewState(
            isDarkMode = false,
            isLoading = false,
            isServerActive = false,
            isSynchronizeSuccess = false,
            badges = emptyList(),
            error = null
        )
    }
}
