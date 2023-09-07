package com.socialite.data.schema.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
        tableName = Outcome.DB_NAME,
        indices = [
            Index(value = [Outcome.ID])
        ]
)
data class Outcome(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = PRICE)
        var price: Long,

        @ColumnInfo(name = AMOUNT)
        var amount: Int,

        @ColumnInfo(name = DATE)
        var date: String,

        @ColumnInfo(name = STORE)
        var store: Long,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {
    companion object {
        const val ID = "id_outcome"
        const val AMOUNT = "amount"
        const val PRICE = "price"
        const val NAME = "name"
        const val DESC = "desc"
        const val DATE = "date"
        const val STORE = "store"
        const val DB_NAME = "outcome"
    }
}
