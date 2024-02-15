package com.socialite.schema.database.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.socialite.schema.database.EntityData

@Entity(
    tableName = User.DB_NAME,
    primaryKeys = [User.ID],
    indices = [
        Index(value = [User.ID])
    ]
)
data class User(
    @ColumnInfo(name = ID)
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = EMAIL)
    val email: String,

    @ColumnInfo(name = AUTHORITY)
    val authority: String,

    @ColumnInfo(name = ACTIVE, defaultValue = "1")
    val active: Boolean,
) : EntityData {
    companion object {
        const val DB_NAME = "user"

        const val ID = "id_user"
        const val AUTHORITY = "authority"
        const val ACTIVE = "active"
        const val NAME = "name"
        const val EMAIL = "email"
    }
}
