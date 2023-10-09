package com.socialite.schema.ui.main

import java.util.UUID

data class Order(
    val id: String,
    val orderNo: String,
    val customer: String,
    val orderTime: String,
    val isTakeAway: Boolean,
    val status: Int,
    val store: String,
    val user: Long,
    val isUploaded: Boolean
) {

    fun isEditable() = status == ON_PROCESS || status == NEED_PAY

    fun getQueueNumber(): String {
        return orderNo.substring(orderNo.length - 3, orderNo.length)
    }

    companion object {

        const val ON_PROCESS = 0
        const val NEED_PAY = 1
        const val CANCEL = 2
        const val DONE = 3

        fun createNew(
            orderNo: String,
            customer: String,
            orderTime: String,
            store: String,
            isTakeAway: Boolean,
            userId: Long
        ) = Order(
            id = UUID.randomUUID().toString(),
            orderNo = orderNo,
            customer = customer,
            orderTime = orderTime,
            isTakeAway = isTakeAway,
            status = ON_PROCESS,
            store = store,
            isUploaded = false,
            user = userId
        )
    }
}
