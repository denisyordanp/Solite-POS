package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product

data class ProductWithCategory(
    @Embedded
    val product: Product,

    @Relation(parentColumn = Category.ID, entityColumn = Category.ID)
    val category: Category,

	@Ignore
	val hasVariant: Boolean
) {
	constructor(product: Product, category: Category): this(product = product, category = category, hasVariant = false)
}
