package com.socialite.solite_pos.view.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.domain.IsServerActive
import com.socialite.domain.domain.MigrateToUUID
import com.socialite.domain.domain.SetDarkMode
import com.socialite.domain.domain.SetNewToken
import com.socialite.domain.domain.Synchronize
import com.socialite.solite_pos.schema.GeneralMenuBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val synchronize: Synchronize,
    private val migrateToUUID: MigrateToUUID,
    private val setNewToken: SetNewToken,
    private val setDarkMode: SetDarkMode,
    private val isDarkModeActive: IsDarkModeActive,
    private val isServerActive: IsServerActive,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    private val _viewState = MutableStateFlow(SettingViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    isServerActive = isServerActive()
                )
            )

            isDarkModeActive()
                .map {
                    _viewState.value.copy(
                        isDarkMode = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun getBadges(date: String) {
        viewModelScope.launch {
            getOrdersGeneralMenuBadge(date = date).map { menus ->
                menus.map { GeneralMenuBadge.fromDomain(it) }
            }
                .map {
                    _viewState.value.copy(
                        badges = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun setDarkMode(isActive: Boolean) {
        viewModelScope.launch {
            setDarkMode.invoke(isActive)
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
        setNewToken("")
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
