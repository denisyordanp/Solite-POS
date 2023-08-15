package com.socialite.solite_pos.data.schema.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.schema.room.new_master.VariantOption
import java.io.Serializable

data class DetailWithVariantOption(
    @Embedded
	var detail: OrderDetail,

    @Relation(
		parentColumn = OrderDetail.ID,
		entity = VariantOption::class,
		entityColumn = VariantOption.ID,
		associateBy = Junction(
			value = OrderProductVariant::class,
			parentColumn = OrderDetail.ID,
			entityColumn = VariantOption.ID
		)
	) val options: List<VariantOption> = emptyList()
): Serializable
