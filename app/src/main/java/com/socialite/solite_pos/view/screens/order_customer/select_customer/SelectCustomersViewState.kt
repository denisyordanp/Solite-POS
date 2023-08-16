package com.socialite.solite_pos.view.screens.order_customer.select_customer

import com.socialite.data.schema.room.new_master.Customer

data class SelectCustomersViewState(
    val customers: List<Customer>,
    val keyword: String,
) {

    fun getFilteredCustomers() = when {
        keyword.isNotEmpty() -> {
            customers.filter {
                    it.name.contains(keyword, ignoreCase = true)
                }
        }
        else -> customers
    }

    companion object {
        fun idle() = SelectCustomersViewState(
            customers = emptyList(),
            keyword = ""
        )
    }
}
