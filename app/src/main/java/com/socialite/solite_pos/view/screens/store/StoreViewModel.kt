package com.socialite.solite_pos.view.screens.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.domain.GetOrdersGeneralMenuBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    private val _viewState = MutableStateFlow(StoreViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun loadBadges(date: String) {
        viewModelScope.launch {
            getOrdersGeneralMenuBadge(date = date)
                .map {
                    _viewState.value.copy(
                        badges = it
                    )
                }
                .collect(_viewState)
        }
    }
}
