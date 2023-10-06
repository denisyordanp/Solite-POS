package com.socialite.schema.database.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.EntityData

@Entity(
    tableName = Store.DB_NAME,
    indices = [
        Index(value = [Store.ID])
    ]
)
data class Store(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = ADDRESS)
    val address: String,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {
    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_store"
        const val ID_ADD = "add_id"
    }
}

