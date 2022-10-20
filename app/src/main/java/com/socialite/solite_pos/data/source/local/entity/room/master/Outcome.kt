package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.*

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

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object: RemoteClassUtils<Outcome> {
		const val ID = "id_outcome"
		const val AMOUNT = "amount"
		const val PRICE = "price"
		const val NAME = "name"
		const val DESC = "desc"
		const val DATE = "date"

		const val DB_NAME = "outcome"
		override fun toHashMap(data: Outcome): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					DESC to data.desc,
					PRICE to data.price,
					AMOUNT to data.amount,
					DATE to data.date,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<Outcome> {
			val array: ArrayList<Outcome> = ArrayList()
			for (document in result){
				val outcome = Outcome(
						document.data[ID] as Long,
						document.data[NAME] as String,
						document.data[DESC] as String,
						document.data[PRICE] as Long,
						(document.data[AMOUNT] as Long).toInt(),
						document.data[DATE] as String,
						document.data[UPLOAD] as Boolean
				)
				array.add(outcome)
			}
			return array
		}
	}

	@Ignore
	constructor(id: Long, name: String, desc: String, price: Long, amount: Int, date: String): this(id, name, desc, price, amount, date, false)

	@Ignore
	constructor(name: String, desc: String, price: Long, amount: Int, date: String): this(0, name, desc, price, amount, date, false)

	@Ignore
	constructor(name: String, desc: String, price: Long, date: String): this(0, name, desc, price, 1, date, false)

	val total: Long
		get() = price * amount
}
