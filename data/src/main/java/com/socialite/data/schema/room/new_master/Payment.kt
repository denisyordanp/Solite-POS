package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.PaymentResponse
import com.socialite.data.schema.room.EntityData

@Entity(
    tableName = Payment.DB_NAME,
    indices = [
        Index(value = [Payment.ID])
    ]
)
data class Payment(

    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = TAX)
    val tax: Float,

    @ColumnInfo(name = CASH)
    val isCash: Boolean,

    @ColumnInfo(name = STATUS)
    val isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    fun toResponse(): PaymentResponse {
        return PaymentResponse(
            id = id,
            name = name,
            desc = desc,
            tax = tax,
            isCash = isCash,
            isActive = isActive,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_payment"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"
        const val TAX = "tax"

        const val DB_NAME = "new_payment"

        const val ID_ADD = "add_id"
        const val ALL = 2
        const val ACTIVE = 1

        fun filter(state: Int): SimpleSQLiteQuery {
            val query = StringBuilder().append("SELECT * FROM ")
            query.append(DB_NAME)
            when (state) {
                ACTIVE -> {
                    query.append(" WHERE ")
                        .append(STATUS)
                        .append(" = ").append(ACTIVE)
                }
            }
            return SimpleSQLiteQuery(query.toString())
        }
    }
}
