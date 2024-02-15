package com.socialite.schema.database.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.EntityData

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
    val name: String,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    companion object {
        const val ID_ADD = "add_id"

        const val ID = "id_customer"
        const val NAME = "name"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_customer"

        fun add(name: String) = Customer(
            id = ID_ADD,
            name = name,
            isUploaded = false
        )
    }
}
