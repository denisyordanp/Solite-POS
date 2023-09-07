package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import java.io.Serializable

@Entity(
        tableName = Store.DB_NAME,
        indices = [
            Index(value = [Store.ID])
        ]
)
data class Store(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long = 0L,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = ADDRESS)
        var address: String,
) : Serializable {

    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"

        const val DB_NAME = "store"
    }
}
