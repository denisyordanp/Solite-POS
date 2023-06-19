package com.socialite.solite_pos.view.orders.order_items

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.socialite.solite_pos.data.source.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter

class OrderItemsViewModel(
    private val getOrdersMenuWithOrders: GetOrdersMenuWithOrders
) : ViewModel() {

    fun getOrders(parameter: ReportsParameter) = getOrdersMenuWithOrders(parameter)

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrderItemsViewModel(
                    getOrdersMenuWithOrders = LoggedInDomainInjection.provideGetOrdersMenuWithOrders(
                        context
                    )
                ) as T
            }
        }
    }
}
