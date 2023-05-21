package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.*
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.REPLACED_UUID
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable
import java.util.UUID

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

    @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
    val new_id: String,

    @ColumnInfo(name = NAME)
    var name: String,

	@ColumnInfo(name = UPLOAD)
	var isUploaded: Boolean
): Serializable{
	companion object: RemoteClassUtils<Customer> {
		const val ID_ADD = 0L

		const val ID = "id_customer"
		const val NAME = "name"

		const val DB_NAME = "customer"

		override fun toListClass(result: QuerySnapshot): List<Customer> {
			val array: ArrayList<Customer> = ArrayList()
			for (document in result){
				val customer = Customer(
						document.data[ID] as Long,
					document.data[REPLACED_UUID] as String,
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

        fun add(name: String) = Customer(
            id = ID_ADD,
            new_id = UUID.randomUUID().toString(),
            name = name,
            isUploaded = false
        )
	}

	fun isAdd() = id == ID_ADD
}
