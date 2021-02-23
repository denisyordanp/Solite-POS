package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
		tableName = Outcome.DB_NAME,
		indices = [
			Index(value = [Outcome.ID])
		]
)
data class Outcome(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = PRICE)
		var price: Int,

		@ColumnInfo(name = AMOUNT)
		var amount: Int,

		@ColumnInfo(name = DATE)
		var date: String,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object{
		const val ID = "id_outcome"
		const val AMOUNT = "amount"
		const val PRICE = "price"
		const val NAME = "name"
		const val DESC = "desc"
		const val DATE = "date"

		const val DB_NAME = "outcome"
	}

	constructor(name: String, desc: String, price: Int, amount: Int, date: String): this(0, name, desc, price, amount, date, false)

	val total: Int
		get() = price * amount

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					NAME to name,
					DESC to desc,
					PRICE to price,
					AMOUNT to amount,
					DATE to date,
					UPLOAD to isUploaded
			)
		}
}
