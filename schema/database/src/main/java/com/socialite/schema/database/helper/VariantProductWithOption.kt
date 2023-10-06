package com.socialite.schema.database.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.schema.database.new_bridge.VariantProduct
import com.socialite.schema.database.new_master.Variant
import com.socialite.schema.database.new_master.VariantOption
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
