package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import com.socialite.schema.ui.helper.ProductOrderDetail
import com.socialite.schema.ui.main.Customer
import kotlinx.coroutines.flow.Flow

fun interface NewOrder {
    operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    ): Flow<DataState<Boolean>>
}
