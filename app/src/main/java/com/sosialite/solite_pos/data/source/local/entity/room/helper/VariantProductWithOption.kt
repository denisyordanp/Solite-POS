package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import java.io.Serializable

data class VariantProductWithOption(
		@Embedded val variant: Variant,

		@Relation(
				parentColumn = Variant.ID,
				entity = VariantOption::class,
				entityColumn = VariantOption.ID,
				associateBy = Junction(
						value = VariantProduct::class,
						parentColumn = Variant.ID,
						entityColumn = VariantOption.ID
				)
		) val options: List<VariantOption> = emptyList()
) : Serializable
