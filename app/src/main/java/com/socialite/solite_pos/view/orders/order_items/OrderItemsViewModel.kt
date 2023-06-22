package com.socialite.solite_pos.view.orders.order_items

import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderItemsViewModel @Inject constructor(
    private val getOrdersMenuWithOrders: GetOrdersMenuWithOrders,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    fun getOrders(parameter: ReportParameter) = getOrdersMenuWithOrders(parameter)
    fun getBadges(date: String) = getOrdersGeneralMenuBadge(date = date)
}
