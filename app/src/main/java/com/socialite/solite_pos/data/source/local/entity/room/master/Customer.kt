package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable

@Entity(
	tableName = Customer.DB_NAME,
	indices = [
		Index(value = [Customer.ID]),
	]
)
data class Customer(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ID)
	var id: Long,

	@ColumnInfo(name = NAME)
	var name: String,

	@ColumnInfo(name = UPLOAD)
	var isUploaded: Boolean
): Serializable{
	companion object: RemoteClassUtils<Customer> {
		const val ID_ADD = -1L

		const val ID = "id_customer"
		const val NAME = "name"

		const val DB_NAME = "customer"

		override fun toListClass(result: QuerySnapshot): List<Customer> {
			val array: ArrayList<Customer> = ArrayList()
			for (document in result){
				val customer = Customer(
						document.data[ID] as Long,
						document.data[NAME] as String,
						document.data[UPLOAD] as Boolean
				)
				array.add(customer)
			}
			return array
		}

		override fun toHashMap(data: Customer): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					UPLOAD to data.isUploaded
			)
		}

	}

	constructor(name: String): this(0, name, false)
}
