package com.socialite.solite_pos.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.response.StoreResponse
import com.socialite.solite_pos.data.schema.room.EntityData
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable
import java.util.UUID

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
        override var name: String,

        @ColumnInfo(name = ADDRESS)
        var address: String,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable, DropdownItem, EntityData {

    fun isNewStore() = id == ID_ADD

    fun asNewStore() = this.copy(
            id = UUID.randomUUID().toString()
    )

    fun toResponse(): StoreResponse {
        return StoreResponse(
                id = id,
                name = name,
                address = address,
                isUploaded = true
        )
    }

    companion object {
        const val ID = "id_store"
        const val NAME = "name"
        const val ADDRESS = "address"

        const val DB_NAME = "new_store"
        const val ID_ADD = "add_id"

        fun add(name: String, address: String): Store {
            return Store(
                    id = ID_ADD,
                    name = name,
                    address = address,
                    isUploaded = false
            )
        }
    }
}
