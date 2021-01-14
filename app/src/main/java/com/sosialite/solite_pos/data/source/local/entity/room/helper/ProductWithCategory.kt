package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product

data class ProductWithCategory(
	@Embedded
	var product: Product? = null,

	@Relation(parentColumn = Category.ID, entityColumn = Category.ID)
	var category: Category? = null,
)
