package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD

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
) {

    companion object {
        const val ID = "id_customer"
        const val NAME = "name"

        const val DB_NAME = "new_customer"
    }
}
