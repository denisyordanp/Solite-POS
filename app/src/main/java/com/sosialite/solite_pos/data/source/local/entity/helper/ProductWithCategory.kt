package com.sosialite.solite_pos.data.source.local.entity.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Product

data class ProductWithCategory(
	@Embedded
	var product: Product? = null,

	@Relation(parentColumn = Category.ID, entityColumn = Category.ID)
	var category: Category? = null
)
