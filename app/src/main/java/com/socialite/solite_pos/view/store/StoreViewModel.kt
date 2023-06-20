package com.socialite.solite_pos.view.store

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StoreViewModel(
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

    companion object {
        fun getFactory(activity: FragmentActivity) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StoreViewModel(
                    getOrdersGeneralMenuBadge = LoggedInDomainInjection.provideGetOrdersGeneralMenuBadge(
                        activity
                    )
                ) as T
            }
        }
    }
}
