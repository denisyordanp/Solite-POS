package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import java.io.Serializable

data class VariantProductWithOption(
		@Embedded val variantProduct: VariantProduct,

		@Relation(
				parentColumn = Variant.ID,
				entityColumn = Variant.ID,
		) val variant: Variant,

		@Relation(
				parentColumn = VariantOption.ID,
				entityColumn = VariantOption.ID,
		) val option: VariantOption
) : Serializable
