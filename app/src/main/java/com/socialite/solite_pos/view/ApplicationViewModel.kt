package com.socialite.solite_pos.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.IsDarkModeActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ApplicationViewModel(
    application: Application,
    private val isDarkModeActive: IsDarkModeActive
) : AndroidViewModel(application) {

    fun getDarkMode(isDarkMode: (Boolean) -> Unit) {
        viewModelScope.launch {
            val darkMode = isDarkModeActive().first()
            isDarkMode(darkMode)
        }
    }
}
