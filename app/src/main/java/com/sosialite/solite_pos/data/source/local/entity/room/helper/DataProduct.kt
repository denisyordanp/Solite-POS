package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

data class DataProduct(
	@Embedded
	var product: Product,

	@Relation(parentColumn = Category.ID, entityColumn = Category.ID)
	var category: Category,

	@Relation(
		parentColumn = Product.ID,
		entity = VariantOption::class,
		entityColumn = VariantOption.ID,
		associateBy = Junction(
			value = VariantProduct::class,
			parentColumn = Product.ID,
			entityColumn = VariantOption.ID
		)
	) val options: List<VariantWithOption> = emptyList()
): Serializable
