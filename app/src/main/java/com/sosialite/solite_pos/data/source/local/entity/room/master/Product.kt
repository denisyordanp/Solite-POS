package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
		tableName = Product.DB_NAME,
		foreignKeys = [ForeignKey(
				entity = Category::class,
				parentColumns = [Category.ID],
				childColumns = [Category.ID],
				onDelete = CASCADE)
		],
		indices = [
			Index(value = [
				Product.ID,
				Category.ID
			])
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

		@ColumnInfo(name = SELL_PRICE)
		var sellPrice: Int,

		@ColumnInfo(name = BUY_PRICE)
		var buyPrice: Int,

		@ColumnInfo(name = PORTION)
		var portion: Int,

		@ColumnInfo(name = STOCK)
		var stock: Int,

		@ColumnInfo(name = MIX)
		var isMix: Boolean,

		@ColumnInfo(name = STATUS)
		var isActive: Boolean,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object{
		const val SELL_PRICE = "sell_price"
		const val BUY_PRICE = "buy_price"
		const val PORTION = "portion"
		const val ID = "id_product"
		const val STATUS = "status"
		const val STOCK = "stock"
		const val NAME = "name"
		const val DESC = "desc"
		const val MIX = "mix"

		const val DB_NAME = "product"
	}

	constructor(name: String, category: Int, desc: String, sellPrice: Int, buyPrice: Int, portion: Int, stock: Int, isMix: Boolean, isActive: Boolean): this(0, name, category, desc, sellPrice, buyPrice, portion, stock, isMix, isActive, false)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					NAME to name,
					Category.ID to category,
					DESC to desc,
					SELL_PRICE to sellPrice,
					BUY_PRICE to buyPrice,
					PORTION to portion,
					STOCK to stock,
					MIX to isMix,
					STATUS to isActive,
					UPLOAD to isUploaded
			)
		}
}
