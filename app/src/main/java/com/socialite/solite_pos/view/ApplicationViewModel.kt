package com.socialite.solite_pos.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.repository.SettingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ApplicationViewModel(
    application: Application,
    private val settingRepository: SettingRepository
) : AndroidViewModel(application) {

    fun getDarkMode(isDarkMode: (Boolean) -> Unit) {
        viewModelScope.launch {
            val darkMode = settingRepository.getIsDarkModeActive().first()
            isDarkMode(darkMode)
        }
    }
}
