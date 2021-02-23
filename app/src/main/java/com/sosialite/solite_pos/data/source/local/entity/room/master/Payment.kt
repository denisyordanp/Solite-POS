package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
		tableName = Payment.DB_NAME,
		indices = [
			Index(value = [Payment.ID])
		]
)
data class Payment(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = TAX)
		var tax: Float,

		@ColumnInfo(name = CASH)
		var isCash: Boolean,

		@ColumnInfo(name = STATUS)
		var isActive: Boolean,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object{
		const val ID = "id_payment"
		const val STATUS = "status"
		const val NAME = "name"
		const val DESC = "desc"
		const val CASH = "cash"
		const val TAX = "tax"

		const val DB_NAME = "payment"
	}

	constructor(name: String, desc: String, tax: Float, isCash: Boolean, isActive: Boolean): this(0, name, desc, tax, isCash, isActive, false)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					NAME to name,
					DESC to desc,
					TAX to tax,
					CASH to isCash,
					STATUS to isActive,
					UPLOAD to isUploaded
			)
		}
}
