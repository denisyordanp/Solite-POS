package com.socialite.solite_pos.view.screens.order_customer.select_items

import androidx.lifecycle.ViewModel
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.solite_pos.schema.GeneralMenuBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SelectItemsViewModel @Inject constructor(
    private val getProductWithCategories: GetProductWithCategories,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    fun getAllProducts() = getProductWithCategories()

    fun getBadges(date: String) = getOrdersGeneralMenuBadge(date = date).map { menus ->
        menus.map { GeneralMenuBadge.fromDomain(it) }
    }

}
