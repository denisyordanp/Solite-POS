package com.socialite.solite_pos.view.store.product_master

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product

data class ProductItemViewData(
    val product: Product,
    val variantCount: Int
)
