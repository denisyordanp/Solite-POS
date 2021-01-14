package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import java.io.Serializable

@Entity(
	tableName = AppDatabase.TBL_PRODUCT,
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
	@PrimaryKey(autoGenerate = true)
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
	var stock: Int,

	@ColumnInfo(name = STATUS)
	var isActive: Boolean
): Serializable{
	companion object{
		const val ID = "id_product"
		const val STATUS = "status"
		const val STOCK = "stock"
		const val PRICE = "price"
		const val NAME = "name"
		const val DESC = "desc"
	}

	constructor(name: String, category: Int, desc: String, price: Int, stock: Int, isActive: Boolean): this(0, name, category, desc, price, stock, isActive)
}
