package com.socialite.schema.ui.main

import java.util.UUID

data class OrderPromo(
    val id: String,
    val order: String,
    val promo: String,
    val totalPromo: Long,
    val isUpload: Boolean
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
