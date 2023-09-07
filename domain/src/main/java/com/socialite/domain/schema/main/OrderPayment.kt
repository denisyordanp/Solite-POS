package com.socialite.domain.schema.main

import java.util.UUID

data class OrderPayment(
    var id: String,
    var order: String,
    var payment: String,
    var pay: Long,
    var isUpload: Boolean
) {

    fun inReturn(total: Long): Long {
        return pay - total
    }

    companion object {

        fun createNew(
            order: String, payment: String, pay: Long
        ) = OrderPayment(
            id = UUID.randomUUID().toString(),
            order = order,
            payment = payment,
            pay = pay,
            isUpload = false
        )
    }
}
