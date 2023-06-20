package com.socialite.solite_pos.view.order_customer.select_items

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection

class SelectItemsViewModel(
    private val getProductWithCategories: GetProductWithCategories,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    fun getAllProducts() = getProductWithCategories()

    fun getBadges(date: String) = getOrdersGeneralMenuBadge(date)

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SelectItemsViewModel(
                    getProductWithCategories = LoggedInDomainInjection.provideGetProductWithCategories(
                        context
                    ),
                    getOrdersGeneralMenuBadge = LoggedInDomainInjection.provideGetOrdersGeneralMenuBadge(
                        context
                    )
                ) as T
            }
        }
    }
}
