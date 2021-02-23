package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class ProductMixOrderDetail(
        var product: Product,
        var variants: ArrayList<VariantOption>,
        var amount: Int,
): Serializable
