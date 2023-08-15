package com.socialite.solite_pos.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
    tableName = User.DB_NAME,
    primaryKeys = [User.ID],
    indices = [
        Index(value = [User.ID])
    ]
)
data class User(
    @ColumnInfo(name = ID)
    var id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = EMAIL)
    var email: String,

    @ColumnInfo(name = AUTHORITY)
    var authority: String,
) : Serializable {
    companion object {
        const val ID = "id_user"
        const val AUTHORITY = "authority"
        const val NAME = "name"
        const val EMAIL = "email"

        private const val CASHIER = "cashier"
        const val ADMIN = "admin"

        const val DB_NAME = "user"

        fun isNotAdmin(authority: String?): Boolean {
            return authority == CASHIER
        }
    }
}
