package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class DataProductResponse(
        var categories: List<Category>,
        var products: List<Product>,
        var variants: List<Variant>,
        var variantOptions: List<VariantOption>
){
    constructor(): this(emptyList(), emptyList(), emptyList(), emptyList())
}