package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
		tableName = PurchaseProduct.DB_NAME,
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
		var amount: Int,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable {

	constructor(purchaseNo: String, idProduct: Int, amount: Int): this(0, purchaseNo, idProduct, amount, false)

	companion object{
		const val ID = "id_purchase_product"
		const val AMOUNT = "amount"

		const val DB_NAME = "purchase_product"
	}

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					Purchase.NO to purchaseNo,
					Product.ID to idProduct,
					AMOUNT to amount,
					UPLOAD to isUploaded
			)
		}
}
