package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Product

data class ProductResponse(
    val buyPrice: Int,
    val category: Int,
    val desc: String,
    val id: String,
    val image: String,
    val isActive: Boolean,
    val isMix: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val portion: Int,
    val sellPrice: Int,
    val stock: Int
) {
    fun toEntity(): Product {
        return Product(
            id = id.toLong(),
            name = name,
            category = category.toLong(),
            image = image,
            desc = desc,
            sellPrice = sellPrice.toLong(),
            buyPrice = buyPrice.toLong(),
            portion = portion,
            stock = stock.toLong(),
            isMix = isMix,
            isActive = isActive,
            isUploaded = isUploaded
        )
    }
}
