package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.util.*

@Entity(
		tableName = Supplier.DB_NAME,
		indices = [
			Index(value = [Supplier.ID]),
		]
)
data class Supplier(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = ID)
		var id: Long,

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
	companion object: RemoteUtils<Supplier>{
		const val ID = "id_supplier"
		const val ADDRESS = "address"
		const val PHONE = "phone"
		const val NAME = "name"
		const val DESC = "desc"

		const val DB_NAME = "supplier"

		override fun toHashMap(data: Supplier): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					PHONE to data.phone,
					ADDRESS to data.address,
					DESC to data.desc,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<Supplier> {
			val array: ArrayList<Supplier> = ArrayList()
			for (document in result){
				val supplier = Supplier(
						document.data[ID] as Long,
						document.data[NAME] as String,
						document.data[PHONE] as String,
						document.data[ADDRESS] as String,
						document.data[DESC] as String,
						document.data[UPLOAD] as Boolean
				)
				array.add(supplier)
			}
			return array
		}
	}

	constructor(id: Long, name: String, phone: String, address: String, desc: String): this(id, name, phone, address, desc, false)
	constructor(name: String, phone: String, address: String, desc: String): this(0, name, phone, address, desc, false)
}
