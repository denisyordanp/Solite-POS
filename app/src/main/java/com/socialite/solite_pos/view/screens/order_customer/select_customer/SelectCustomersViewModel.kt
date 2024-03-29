package com.socialite.solite_pos.view.screens.order_customer.select_customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetCustomers
import com.socialite.domain.domain.NewCustomer
import com.socialite.domain.schema.main.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCustomersViewModel @Inject constructor(
    private val newCustomer: NewCustomer,
    private val getCustomers: GetCustomers,
) : ViewModel() {

    private val _viewState = MutableStateFlow(SelectCustomersViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getCustomers()
                .map { customers ->
                    _viewState.value.copy(
                        customers = customers
                    )
                }
                .collect(_viewState)
        }
    }

    fun searchCustomer(keyword: String) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    keyword = keyword
                )
            )
        }
    }

    fun newCustomer(data: Customer) {
        viewModelScope.launch {
            newCustomer.invoke(data)
        }
    }
}
