package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.tools.RemoteUtils
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
			Index(value = [Product.ID]),
			Index(value = [Category.ID])
		]
)
data class Product(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = Category.ID)
		var category: Long,

		@ColumnInfo(name = IMAGE)
		var image: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = SELL_PRICE)
		var sellPrice: Long,

		@ColumnInfo(name = BUY_PRICE)
		var buyPrice: Long,

		@ColumnInfo(name = PORTION)
		var portion: Int,

		@ColumnInfo(name = STOCK)
		var stock: Long,

		@ColumnInfo(name = MIX)
		var isMix: Boolean,

		@ColumnInfo(name = STATUS)
		var isActive: Boolean,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object: RemoteUtils<Product>{
		const val SELL_PRICE = "sell_price"
		const val BUY_PRICE = "buy_price"
		const val PORTION = "portion"
		const val ID = "id_product"
		const val STATUS = "status"
		const val STOCK = "stock"
		const val IMAGE = "image"
		const val NAME = "name"
		const val DESC = "desc"
		const val MIX = "mix"

		const val DB_NAME = "product"
		override fun toHashMap(data: Product): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					Category.ID to data.category,
					IMAGE to data.image,
					DESC to data.desc,
					SELL_PRICE to data.sellPrice,
					BUY_PRICE to data.buyPrice,
					PORTION to data.portion,
					STOCK to data.stock,
					MIX to data.isMix,
					STATUS to data.isActive,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<Product> {
			val array: ArrayList<Product> = ArrayList()
			for (document in result){
				val product = Product(
						document.data[ID] as Long,
						document.data[NAME] as String,
						document.data[Category.ID] as Long,
						document.data[IMAGE] as String,
						document.data[DESC] as String,
						document.data[SELL_PRICE] as Long,
						document.data[BUY_PRICE] as Long,
						(document.data[PORTION] as Long).toInt(),
						document.data[STOCK] as Long,
						document.data[MIX] as Boolean,
						document.data[STATUS] as Boolean,
						document.data[UPLOAD] as Boolean
				)
				array.add(product)
			}
			return array
		}
	}

	constructor(id: Long, name: String, category: Long, image: String, desc: String, sellPrice: Long, buyPrice: Long, portion: Int, stock: Long, isMix: Boolean, isActive: Boolean): this(id, name, category, image, desc, sellPrice, buyPrice, portion, stock, isMix, isActive, false)
	constructor(name: String, category: Long, image: String, desc: String, sellPrice: Long, buyPrice: Long, portion: Int, stock: Long, isMix: Boolean, isActive: Boolean): this(0, name, category, image, desc, sellPrice, buyPrice, portion, stock, isMix, isActive, false)

	fun getStockPortion(): Long {
		return stock / portion
	}
}
