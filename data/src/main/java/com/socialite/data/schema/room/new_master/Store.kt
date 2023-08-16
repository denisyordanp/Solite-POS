package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase

@Entity(
    tableName = Store.DB_NAME,
    indices = [
        Index(value = [Store.ID])
    ]
)
data class Store(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = ADDRESS)
    var address: String,

    @ColumnInfo(name = AppDatabase.UPLOAD)
    var isUploaded: Boolean
) {
    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"

        const val DB_NAME = "new_store"
    }
}
