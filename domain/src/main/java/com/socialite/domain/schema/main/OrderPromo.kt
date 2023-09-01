package com.socialite.domain.schema.main

import java.util.UUID

data class OrderPromo(
    var id: String,
    var order: String,
    var promo: String,
    var totalPromo: Long,
    var isUpload: Boolean
) {

    companion object {
        fun newPromo(
            orderId: String,
            promo: String,
            totalPromo: Long
        ) = OrderPromo(
            id = UUID.randomUUID().toString(),
            order = orderId,
            promo = promo,
            totalPromo = totalPromo,
            isUpload = false
        )
    }
}
