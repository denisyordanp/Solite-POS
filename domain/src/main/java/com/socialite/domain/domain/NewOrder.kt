package com.socialite.domain.domain

import com.socialite.common.state.DataState
import com.socialite.domain.schema.ProductOrderDetail
import com.socialite.domain.schema.main.Customer
import kotlinx.coroutines.flow.Flow

fun interface NewOrder {
    operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    ): Flow<DataState<Boolean>>
}
