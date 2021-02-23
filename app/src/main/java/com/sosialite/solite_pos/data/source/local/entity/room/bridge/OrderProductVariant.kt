package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import java.io.Serializable

@Entity(
	tableName = OrderProductVariant.DB_NAME,
	foreignKeys = [
		ForeignKey(
			entity = OrderDetail::class,
			parentColumns = [OrderDetail.ID],
			childColumns = [OrderDetail.ID],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = VariantOption::class,
			parentColumns = [VariantOption.ID],
			childColumns = [VariantOption.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [
			OrderProductVariant.ID,
			OrderDetail.ID,
			VariantOption.ID
		])
	]
)
data class OrderProductVariant(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = OrderDetail.ID)
		var idOrderDetail: Int,

		@ColumnInfo(name = VariantOption.ID)
		var idVariantOption: Int
): Serializable{
	companion object{
		const val ID = "id_order_product_variant"

		const val DB_NAME = "order_product_variant"
	}

	constructor(idOrderDetail: Int, idVariantOption: Int): this(0, idOrderDetail, idVariantOption)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
				ID to id,
				OrderDetail.ID to idOrderDetail,
				VariantOption.ID to idVariantOption
			)
		}
}
