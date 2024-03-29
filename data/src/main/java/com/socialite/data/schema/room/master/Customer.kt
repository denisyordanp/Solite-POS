package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.REPLACED_UUID
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
        tableName = Customer.DB_NAME,
        indices = [
            Index(value = [Customer.ID]),
        ]
)
data class Customer(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {
    companion object {
        const val ID = "id_customer"
        const val NAME = "name"

        const val DB_NAME = "customer"
    }
}
