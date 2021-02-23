package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

@Entity(
		tableName = OrderMixProductVariant.DB_NAME,
		foreignKeys = [
			ForeignKey(
					entity = OrderProductVariantMix::class,
					parentColumns = [OrderProductVariantMix.ID],
					childColumns = [OrderProductVariantMix.ID],
					onDelete = ForeignKey.CASCADE),
			ForeignKey(
					entity = VariantOption::class,
					parentColumns = [VariantOption.ID],
					childColumns = [VariantOption.ID],
					onDelete = ForeignKey.CASCADE)
		],
		indices = [
			Index(value = [
				OrderMixProductVariant.ID,
				OrderProductVariantMix.ID,
				VariantOption.ID
			])
		]
)
data class OrderMixProductVariant(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = OrderProductVariantMix.ID)
		var idOrderProductMixVariant: Int,

		@ColumnInfo(name = VariantOption.ID)
		var idVariantOption: Int
): Serializable{
	companion object{
		const val ID = "id_order_mix_product_variant"

		const val DB_NAME = "order_mix_product_variant"
	}

	constructor(idOrderProductMixVariant: Int, idVariantOption: Int): this(0, idOrderProductMixVariant, idVariantOption)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					OrderProductVariantMix.ID to idOrderProductMixVariant,
					VariantOption.ID to idVariantOption
			)
		}
}
