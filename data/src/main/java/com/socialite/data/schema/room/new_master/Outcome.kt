package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.OutcomeResponse
import com.socialite.data.schema.room.EntityData

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

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    fun toResponse(): OutcomeResponse {
        return OutcomeResponse(
            id = id,
            name = name,
            desc = desc,
            date = date,
            amount = amount,
            price = price.toInt(),
            store = store,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_outcome"
        const val AMOUNT = "amount"
        const val PRICE = "price"
        const val NAME = "name"
        const val DESC = "desc"
        const val DATE = "date"

        const val DB_NAME = "new_outcome"
    }
}
