package com.socialite.solite_pos.view.screens.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.state.DataState
import com.socialite.domain.domain.GetLoggedInUser
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetSelectedStore
import com.socialite.domain.domain.GetStoreMenus
import com.socialite.solite_pos.schema.GeneralMenuBadge
import com.socialite.solite_pos.utils.tools.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val getLoggedInUser: GetLoggedInUser,
    private val getStoreMenus: GetStoreMenus,
    private val getSelectedStore: GetSelectedStore
) : ViewModel() {

    private val _viewState = MutableStateFlow(StoreViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun loadData(date: String) = viewModelScope.launch {
        _viewState.emitAll(
            combine(
                getOrdersGeneralMenuBadge(date),
                getStoreMenus(),
                getLoggedInUser(),
                getSelectedStore()
            ) { generalMenus, storeMenus, userState, store ->
                val menus = generalMenus.map { GeneralMenuBadge.fromDomain(it) }
                val user = if (userState is DataState.Success) userState.data else null

                _viewState.value.copy(
                    badges = menus,
                    menus = storeMenus,
                    user = user?.toUi(),
                    store = store?.toUi()
                )
            }
        )
    }
}
