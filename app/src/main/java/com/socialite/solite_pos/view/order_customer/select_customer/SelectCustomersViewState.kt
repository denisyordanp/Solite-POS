package com.socialite.solite_pos.view.order_customer.select_customer

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer

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