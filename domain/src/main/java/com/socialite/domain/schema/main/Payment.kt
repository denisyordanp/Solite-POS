package com.socialite.domain.schema.main

import java.util.UUID

data class Payment(
    val id: String,
    val name: String,
    val desc: String,
    val tax: Float,
    val isCash: Boolean,
    val isActive: Boolean,
    val isUploaded: Boolean
) {

    fun isNewPayment() = id == ID_ADD

    fun asNewPayment() = this.copy(
        id = UUID.randomUUID().toString()
    )

    companion object {
        const val ID_ADD = "add_id"

        fun createNewPayment(
            name: String,
            desc: String,
            isCash: Boolean
        ) = Payment(
            id = ID_ADD,
            name = name,
            desc = desc,
            tax = 0f,
            isCash = isCash,
            isActive = true,
            isUploaded = false
        )
    }

    enum class Status(val code: Int) {
        ALL(2), ACTIVE(1)
    }
}
