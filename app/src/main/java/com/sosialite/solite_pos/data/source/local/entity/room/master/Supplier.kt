package com.sosialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
		tableName = Supplier.DB_NAME,
		indices = [
			Index(value = [Supplier.ID]),
		]
)
data class Supplier(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Int,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = PHONE)
		var phone: String,

		@ColumnInfo(name = ADDRESS)
		var address: String,

		@ColumnInfo(name = DESC)
		var desc: String,

		@ColumnInfo(name = UPLOAD)
		var isUploaded: Boolean
): Serializable{
	companion object{
		const val ID_ADD = -1

		const val ID = "id_supplier"
		const val ADDRESS = "address"
		const val PHONE = "phone"
		const val NAME = "name"
		const val DESC = "desc"

		const val DB_NAME = "supplier"
	}

	constructor(name: String, phone: String, address: String, desc: String): this(0, name, phone, address, desc, false)

	val hashMap: HashMap<String, Any?>
		get() {
			return hashMapOf(
					ID to id,
					NAME to name,
					PHONE to phone,
					ADDRESS to address,
					DESC to desc,
					UPLOAD to isUploaded
			)
		}
}
