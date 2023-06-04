package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment

data class PaymentResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCash: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val tax: Float
) {

    fun toEntity(): Payment {
        return Payment(
            id = id,
            name = name,
            desc = desc,
            tax = tax,
            isCash = isCash,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
