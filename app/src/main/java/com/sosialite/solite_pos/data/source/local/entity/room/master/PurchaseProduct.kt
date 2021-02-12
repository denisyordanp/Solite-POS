package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_PURCHASE_PRODUCT,
		foreignKeys = [
			ForeignKey(
					entity = Purchase::class,
					parentColumns = [Purchase.NO],
					childColumns = [Purchase.NO],
					onDelete = ForeignKey.CASCADE
			),
			ForeignKey(
					entity = Product::class,
					parentColumns = [Product.ID],
					childColumns = [Product.ID],
					onDelete = ForeignKey.CASCADE
			)
		],
		indices = [
			Index(value = [
				PurchaseProduct.ID,
				Purchase.NO,
				Product.ID
			])
		]
)
data class PurchaseProduct(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Int,

	@ColumnInfo(name = Purchase.NO)
	var purchaseNo: String,

	@ColumnInfo(name = Product.ID)
	var idProduct: Int,

	@ColumnInfo(name = AMOUNT)
	var amount: Int
): Serializable {

	constructor(purchaseNo: String, idProduct: Int, amount: Int): this(0, purchaseNo, idProduct, amount)

	companion object{
		const val ID = "id_purchase_product"
		const val AMOUNT = "amount"
	}
}
