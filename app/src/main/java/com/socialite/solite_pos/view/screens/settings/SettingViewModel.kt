package com.socialite.solite_pos.view.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.state.DataState
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.domain.IsServerActive
import com.socialite.domain.domain.Logout
import com.socialite.domain.domain.MigrateToUUID
import com.socialite.domain.domain.SetDarkMode
import com.socialite.domain.domain.Synchronize
import com.socialite.solite_pos.schema.GeneralMenuBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val synchronize: Synchronize,
    private val migrateToUUID: MigrateToUUID,
    private val logout: Logout,
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

    fun beginSynchronize() = viewModelScope.launch {
        migrateToUUID()

        _viewState.emitAll(
            synchronize().map {
                when (it) {
                    is DataState.Error -> _viewState.value.copy(error = it.errorState, isLoading = false)
                    DataState.Loading -> _viewState.value.copy(isLoading = true)
                    is DataState.Success -> _viewState.value.copy(
                        isLoading = false,
                        isSynchronizeSuccess = true
                    )

                    else -> viewState.value
                }
            }
        )
    }

    fun logout() {
        logout.invoke()
    }

    fun resetSynchronizeStatus() {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    isSynchronizeSuccess = false,
                    error = null,
                    isLoading = false
                )
            )
        }
    }
}
