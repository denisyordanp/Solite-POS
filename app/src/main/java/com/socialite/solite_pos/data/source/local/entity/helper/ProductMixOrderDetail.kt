package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class ProductMixOrderDetail(
        var product: Product,
        var variants: ArrayList<VariantOption>,
        var amount: Int,
): Serializable
