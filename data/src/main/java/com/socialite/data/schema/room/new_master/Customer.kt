package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.CustomerResponse
import com.socialite.data.schema.room.EntityData

@Entity(
    tableName = Customer.DB_NAME,
    indices = [
        Index(value = [Customer.ID]),
    ]
)
data class Customer(

    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : EntityData {

    fun toResponse(): CustomerResponse {
        return CustomerResponse(
            id = id,
            name = name,
            isUploaded = true
        )
    }

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
}
