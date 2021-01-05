package com.sosialite.solite_pos.data.source.local.entity.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
		tableName = AppDatabase.TBL_PRODUCT,
		primaryKeys = [Product.ID],
		foreignKeys = [ForeignKey(
				entity = Category::class,
				parentColumns = [Category.ID],
				childColumns = [Category.ID],
				onDelete = CASCADE)
		],
		indices = [
			Index(value = [Product.ID]),
			Index(value = [Category.ID])
		]
)
data class Product(
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = Category.ID)
		var category: Int,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = PRICE)
		var price: Int,

		@ColumnInfo(name = STOCK)
		var stock: Int
): Serializable{
	companion object{
		const val ID = "id_product"
		const val NAME = "name"
		const val DESC = "desc"
		const val PRICE = "price"
		const val STOCK = "stock"
	}
}
