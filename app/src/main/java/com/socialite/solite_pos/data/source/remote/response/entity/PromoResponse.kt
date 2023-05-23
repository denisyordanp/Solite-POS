package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo

data class PromoResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCash: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val value: Int?
) {

    fun toEntity(): Promo {
        return Promo(
            id = id,
            name = name,
            desc = desc,
            isCash = isCash,
            value = value,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
