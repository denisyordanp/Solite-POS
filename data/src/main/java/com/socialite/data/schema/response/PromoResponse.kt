package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.Promo

data class PromoResponse(
    val desc: String,
    override val id: String,
    val isActive: Boolean,
    val isCash: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val value: Int?
) : ResponseData {

    override fun toEntity(): Promo {
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
