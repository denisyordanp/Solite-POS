package com.socialite.solite_pos.view.settings

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.view.factory.LoggedInViewModelFromFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingViewModel(
    private val synchronize: Synchronize,
    private val migrateToUUID: MigrateToUUID,
    private val settingRepository: SettingRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    companion object : LoggedInViewModelFromFactory<SettingViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): SettingViewModel {
            return buildViewModel(activity, SettingViewModel::class.java)
        }
    }

    private val _viewState = MutableStateFlow(SettingViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val isServerActive = remoteConfigRepository.isServerActive()
            _viewState.emit(
                _viewState.value.copy(
                    isServerActive = isServerActive
                )
            )

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
                migrateToUUID()
                val isSuccess = synchronize()
                if (isSuccess) emit(
                    currentState.copy(
                        isLoading = false,
                        isSynchronizeSuccess = true
                    )
                )
            }.onStart {
                emit(currentState.copy(isLoading = true))
            }.catch {
                emit(currentState.copy(error = it))
            }.collect(_viewState)
        }
    }

    fun logout() {
        settingRepository.insertToken("")
    }

    fun resetSynchronizeStatus() {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    isSynchronizeSuccess = false,
                    error = null
                )
            )
        }
    }

}