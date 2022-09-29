package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct

interface NewOrder {
    suspend operator fun invoke(order: OrderWithProduct)
}
