package com.socialite.solite_pos.view.settings

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.view.factory.ViewModelFromFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingViewModel(
    private val synchronize: Synchronize,
    private val settingRepository: SettingRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    companion object : ViewModelFromFactory<SettingViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): SettingViewModel {
            return buildViewModel(activity, SettingViewModel::class.java)
        }
    }

    private val _viewState = MutableStateFlow(SettingViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            settingRepository.getIsDarkModeActive()
                .map {
                    _viewState.value.copy(
                        isDarkMode = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun setDarkMode(isActive: Boolean) {
        viewModelScope.launch {
            settingRepository.setDarkMode(isActive)
        }
    }

    fun beginSynchronize() {
        viewModelScope.launch {
            val currentState = _viewState.value

            flow {
                val isSuccess = synchronize()
                if (isSuccess) emit(currentState.copy(isLoading = false))
            }.onStart {
                emit(currentState.copy(isLoading = true))
            }.catch {
                emit(currentState.copy(error = it))
            }.collect(_viewState)
        }
    }

    fun logout() {
        accountRepository.insertToken("")
    }

}