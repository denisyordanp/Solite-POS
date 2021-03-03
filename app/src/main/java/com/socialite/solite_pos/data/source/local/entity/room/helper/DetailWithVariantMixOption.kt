package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import java.io.Serializable

data class DetailWithVariantMixOption(
		@Embedded
		var detail: OrderDetail,

		@Relation(parentColumn = OrderDetail.ID, entityColumn = OrderDetail.ID)
		var variantsMix: List<OrderProductVariantMix> = emptyList()
): Serializable
