package com.sosialite.solite_pos.data.source.remote.response.entity

import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class DataProductResponse(
        var products: List<Product>,
        var variants: List<VariantOption>,
        var categories: List<Category>
){
    constructor(): this(emptyList(), emptyList(), emptyList())
}