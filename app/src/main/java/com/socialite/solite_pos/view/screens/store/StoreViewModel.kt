package com.socialite.solite_pos.view.screens.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetStoreMenus
import com.socialite.solite_pos.schema.GeneralMenuBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val getStoreMenus: GetStoreMenus
) : ViewModel() {

    private val _viewState = MutableStateFlow(StoreViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun loadData(date: String) = viewModelScope.launch {
        _viewState.emitAll(
            getOrdersGeneralMenuBadge(date).map { menus ->
                menus.map { GeneralMenuBadge.fromDomain(it) }
            }.combine(getStoreMenus()) { badges, menus ->
                _viewState.value.copy(
                    badges = badges,
                    menus = menus
                )
            }
        )
    }
}
