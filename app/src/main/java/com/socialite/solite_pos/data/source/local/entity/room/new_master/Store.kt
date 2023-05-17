package com.socialite.solite_pos.data.source.local.entity.room.new_master

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
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    override var name: String,

    @ColumnInfo(name = ADDRESS)
    var address: String,

    @ColumnInfo(name = AppDatabase.UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem {

    fun isNewStore() = id == ID_ADD

    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"

        const val DB_NAME = "new_store"
        const val ID_ADD = "add_id"

        fun newStore(name: String, address: String): Store {
            return Store(
                id = ID_ADD,
                name = name,
                address = address,
                isUploaded = false
            )
        }
    }
}
