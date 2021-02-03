package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_PURCHASE_PRODUCT,
	primaryKeys = [PurchaseProduct.ID],
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
	var purchaseNo: Int,

	@ColumnInfo(name = Product.ID)
	var idProduct: Int,

	@ColumnInfo(name = AMOUNT)
	var amount: Int
): Serializable {
	companion object{
		const val ID = "id_purchase_product"
		const val AMOUNT = "amount"
	}
}
