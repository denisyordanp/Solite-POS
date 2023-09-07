package com.socialite.domain.schema.main

import java.util.UUID

data class OrderPayment(
    val id: String,
    val order: String,
    val payment: String,
    val pay: Long,
    val isUpload: Boolean
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
