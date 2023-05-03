package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.entity.OutcomeResponse
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
	}

	@Ignore
	constructor(name: String, desc: String, price: Long, date: String): this(0, UUID.randomUUID().toString(), name, desc, price, 1, date, 0L, false)

	fun dateString() = DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)

	val total: Long
		get() = price * amount

	fun toResponse(): OutcomeResponse {
		return OutcomeResponse(
			id = id.toString(),
			name = name,
			desc = desc,
			date = date,
			amount = amount,
			price = price.toInt(),
			store = store.toInt(),
			isUploaded = true
		)
	}
}
