package com.socialite.solite_pos.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.schema.room.EntityData
import com.socialite.solite_pos.database.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.response.OutcomeResponse
import com.socialite.solite_pos.utils.config.DateUtils
import java.io.Serializable
import java.util.UUID

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
        var name: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = PRICE)
        var price: Long,

        @ColumnInfo(name = AMOUNT)
        var amount: Int,

        @ColumnInfo(name = DATE)
        var date: String,

        @ColumnInfo(name = Store.ID)
        var store: String,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable, EntityData {


    fun dateString() =
            DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)

    val total: Long
        get() = price * amount

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

        fun createNewOutcome(name: String, desc: String, price: Long, date: String) = Outcome(
                id = UUID.randomUUID().toString(),
                name = name,
                desc = desc,
                price = price,
                amount = 1,
                date = date,
                store = "",
                isUploaded = false
        )
    }
}
