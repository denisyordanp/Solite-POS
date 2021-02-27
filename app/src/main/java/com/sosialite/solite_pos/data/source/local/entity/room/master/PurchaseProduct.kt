package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.sosialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.util.*

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
			Index(value = [PurchaseProduct.ID]),
			Index(value = [Purchase.NO]),
			Index(value = [Product.ID])
		]
)
data class PurchaseProduct(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = Purchase.NO)
		var purchaseNo: String,

		@ColumnInfo(name = Product.ID)
		var idProduct: Long,

		@ColumnInfo(name = AMOUNT)
		var amount: Int,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable {

	constructor(purchaseNo: String, idProduct: Long, amount: Int): this(0, purchaseNo, idProduct, amount, false)

	companion object: RemoteUtils<PurchaseProduct>{
		const val ID = "id_purchase_product"
		const val AMOUNT = "amount"

		const val DB_NAME = "purchase_product"

		override fun toHashMap(data: PurchaseProduct): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					Purchase.NO to data.purchaseNo,
					Product.ID to data.idProduct,
					AMOUNT to data.amount,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<PurchaseProduct> {
			val array: ArrayList<PurchaseProduct> = ArrayList()
			for (document in result){
				val purchase = PurchaseProduct(
						document.data[ID] as Long,
						document.data[Purchase.NO] as String,
						document.data[Product.ID] as Long,
						(document.data[AMOUNT] as Long).toInt(),
						document.data[UPLOAD] as Boolean
				)
				array.add(purchase)
			}
			return array
		}
	}
}
