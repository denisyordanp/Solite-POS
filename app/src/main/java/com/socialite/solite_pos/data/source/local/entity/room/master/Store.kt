package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.view.ui.DropdownItem
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
        override var name: String,

        @ColumnInfo(name = ADDRESS)
        var address: String,
) : Serializable, DropdownItem {

    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"

        const val DB_NAME = "store"
    }
}
