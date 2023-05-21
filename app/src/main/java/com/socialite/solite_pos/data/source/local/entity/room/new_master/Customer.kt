package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable
import java.util.UUID

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
	fun isAdd() = id == ID_ADD

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

		fun createNew(name: String) = Customer(
			id = UUID.randomUUID().toString(),
			name = name,
			isUploaded = false
		)
	}
}
