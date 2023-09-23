package com.socialite.solite_pos.view.screens.store.recap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.state.DataState
import com.socialite.domain.domain.GetOrderMenusWithAmount
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.domain.GetStores
import com.socialite.domain.domain.GetUsers
import com.socialite.solite_pos.schema.MenuOrderAmount
import com.socialite.solite_pos.schema.Store
import com.socialite.solite_pos.schema.User
import com.socialite.solite_pos.utils.tools.mapper.toDomain
import com.socialite.solite_pos.utils.tools.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecapViewModel @Inject constructor(
    private val getStores: GetStores,
    private val getUsers: GetUsers,
    private val getRecapData: GetRecapData,
    private val getOrderMenusWithAmount: GetOrderMenusWithAmount
) : ViewModel() {
    private val _viewState = MutableStateFlow(RecapViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getStores()
                .map { stores ->
                    _viewState.value.copy(
                        stores = stores.map { it.toUi() }
                    )
                }.collect(_viewState)
        }

        viewModelScope.launch {
            getUsers()
                .collect { state ->
                    if (state is DataState.Success) {
                        _viewState.emit(
                            _viewState.value.copy(
                                users = state.data.map { it.toUi() }
                            )
                        )
                    }
                }
        }

        _viewState.onEach {
            val parameters = it.getParameters()
            val recap = getRecapData(parameters.toDomain()).first()
            val menusWithAmount =
                getOrderMenusWithAmount(parameters.toDomain()).first().map { menu ->
                    MenuOrderAmount.fromDomain(menu)
                }
            _viewState.emit(
                _viewState.value.copy(
                    recap = recap,
                    menus = menusWithAmount
                )
            )
        }.launchIn(viewModelScope)
    }

    fun selectDate(selectedDate: Pair<String, String>) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    selectedDate = selectedDate
                )
            )
        }
    }

    fun selectStore(store: Store) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    selectedStore = store
                )
            )
        }
    }

    fun selectUser(user: User) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    selectedUser = user
                )
            )
        }
    }
}
