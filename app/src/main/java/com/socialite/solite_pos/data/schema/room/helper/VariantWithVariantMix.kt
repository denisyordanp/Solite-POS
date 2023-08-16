package com.socialite.solite_pos.data.schema.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.solite_pos.data.schema.room.bridge.VariantMix
import com.socialite.solite_pos.data.schema.room.master.Product
import com.socialite.solite_pos.data.schema.room.master.Variant

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
