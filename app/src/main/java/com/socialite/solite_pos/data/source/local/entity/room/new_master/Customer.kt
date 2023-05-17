package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
	tableName = Customer.DB_NAME,
	indices = [
		Index(value = [Customer.ID]),
	]
)
data class Customer(

	@PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    var name: String,

	@ColumnInfo(name = UPLOAD)
	var isUploaded: Boolean
): Serializable{
	companion object {
		const val ID_ADD = "add_id"

		const val ID = "id_customer"
		const val NAME = "name"

		const val DB_NAME = "new_customer"

        fun add(name: String) = Customer(
            id = ID_ADD,
            name = name,
            isUploaded = false
        )
	}

	fun isAdd() = id == ID_ADD

	// TODO: Create a new customer function to add the UUID
}
