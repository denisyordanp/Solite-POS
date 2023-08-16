package com.socialite.solite_pos.view.screens.order_customer.select_items

import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.domain.GetProductWithCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectItemsViewModel @Inject constructor(
    private val getProductWithCategories: GetProductWithCategories,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    fun getAllProducts() = getProductWithCategories()

    fun getBadges(date: String) = getOrdersGeneralMenuBadge(date)
}
