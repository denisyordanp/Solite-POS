package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.config.DateUtils
import java.io.Serializable
import java.util.UUID

@Entity(
		tableName = Outcome.DB_NAME,
		indices = [
			Index(value = [Outcome.ID])
		]
)
data class Outcome(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

		@ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
		val new_id: String,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = PRICE)
		var price: Long,

		@ColumnInfo(name = AMOUNT)
		var amount: Int,

		@ColumnInfo(name = DATE)
		var date: String,

		@ColumnInfo(name = STORE)
		var store: Long,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object {
		const val ID = "id_outcome"
		const val AMOUNT = "amount"
		const val PRICE = "price"
		const val NAME = "name"
		const val DESC = "desc"
		const val DATE = "date"
		const val STORE = "store"

		const val DB_NAME = "outcome"
//		override fun toHashMap(data: Outcome): HashMap<String, Any?> {
//			return hashMapOf(
//					ID to data.id,
//					NAME to data.name,
//					DESC to data.desc,
//					PRICE to data.price,
//					AMOUNT to data.amount,
//					DATE to data.date,
//					UPLOAD to data.isUploaded
//			)
//		}
//
//		override fun toListClass(result: QuerySnapshot): List<Outcome> {
//			val array: ArrayList<Outcome> = ArrayList()
//			for (document in result){
//				val outcome = Outcome(
//						document.data[ID] as Long,
//						document.data[NAME] as String,
//						document.data[DESC] as String,
//						document.data[PRICE] as Long,
//						(document.data[AMOUNT] as Long).toInt(),
//						document.data[DATE] as String,
//						document.data[UPLOAD] as Boolean
//				)
//				array.add(outcome)
//			}
//			return array
//		}
	}

	@Ignore
	constructor(id: Long, name: String, desc: String, price: Long, amount: Int, date: String): this(id, UUID.randomUUID().toString(), name, desc, price, amount, date, 0L, false)

	@Ignore
	constructor(name: String, desc: String, price: Long, amount: Int, date: String): this(0, UUID.randomUUID().toString(), name, desc, price, amount, date, 0L, false)

	@Ignore
	constructor(name: String, desc: String, price: Long, date: String): this(0, UUID.randomUUID().toString(), name, desc, price, 1, date, 0L, false)

	fun dateString() = DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)

	val total: Long
		get() = price * amount
}
