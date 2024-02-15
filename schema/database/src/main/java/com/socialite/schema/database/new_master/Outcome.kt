package com.socialite.schema.database.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.EntityData

@Entity(
    tableName = Outcome.DB_NAME,
    indices = [
        Index(value = [Outcome.ID])
    ]
)
data class Outcome(

    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = PRICE)
    val price: Long,

    @ColumnInfo(name = AMOUNT)
    val amount: Int,

    @ColumnInfo(name = DATE)
    val date: String,

    @ColumnInfo(name = Store.ID)
    val store: String,

    @ColumnInfo(name = USER, defaultValue = "0")
    val user: Long,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    companion object {
        const val ID = "id_outcome"
        const val USER = "user_id"
        const val AMOUNT = "amount"
        const val PRICE = "price"
        const val NAME = "name"
        const val DESC = "desc"
        const val DATE = "date"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_outcome"
    }
}
