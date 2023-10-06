package com.socialite.schema.database.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.schema.database.bridge.VariantMix
import com.socialite.schema.database.master.Product
import com.socialite.schema.database.master.Variant

data class VariantWithVariantMix(
    @Embedded
	var variant: Variant? = null,

    @Relation(
		parentColumn = Variant.ID,
		entity = Product::class,
		entityColumn = Product.ID,
		associateBy = Junction(
			value = VariantMix::class,
			parentColumn = Variant.ID,
			entityColumn = Product.ID
		)
	) val products: List<Product>
)
