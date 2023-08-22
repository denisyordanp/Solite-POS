package com.socialite.solite_pos.view.screens.orders.order_items

import androidx.lifecycle.ViewModel
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.schema.GeneralMenuBadge
import com.socialite.solite_pos.schema.OrderMenuWithOrders
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderItemsViewModel @Inject constructor(
    private val getOrdersMenuWithOrders: GetOrdersMenuWithOrders,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    fun getOrders(parameter: ReportParameter) = getOrdersMenuWithOrders(parameter.toDomainReport())
        .map {orders ->
            orders.map {
                OrderMenuWithOrders.fromDomain(it)
            }
        }
    fun getBadges(date: String) = getOrdersGeneralMenuBadge(date = date).map { menus ->
        menus.map { GeneralMenuBadge.fromDomain(it) }
    }
}
