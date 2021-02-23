package com.sosialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import java.io.Serializable

@Entity(
	tableName = OrderProductVariantMix.DB_NAME,
	foreignKeys = [
		ForeignKey(
			entity = OrderDetail::class,
			parentColumns = [OrderDetail.ID],
			childColumns = [OrderDetail.ID],
			onDelete = ForeignKey.CASCADE),
		ForeignKey(
			entity = Product::class,
			parentColumns = [Product.ID],
			childColumns = [Product.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [
			OrderProductVariantMix.ID,
			OrderDetail.ID,
			Product.ID
		])
	]
)
data class OrderProductVariantMix(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = OrderDetail.ID)
		var idOrderDetail: Int,

		@ColumnInfo(name = Product.ID)
		var idProduct: Int,

		@ColumnInfo(name = AMOUNT)
		var amount: Int
): Serializable{
	companion object{
		const val ID = "id_order_product_variant_mix"
		const val AMOUNT = "amount"

		const val DB_NAME = "order_product_variant_mix"
	}

	constructor(idOrderDetail: Int, idVariantMix: Int, amount: Int): this(0, idOrderDetail, idVariantMix, amount)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					OrderDetail.ID to idOrderDetail,
					Product.ID to idProduct,
					AMOUNT to amount
			)
		}
}
