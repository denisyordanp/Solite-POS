package com.socialite.solite_pos.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.response.PaymentResponse
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable
import java.util.UUID

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
    override var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = TAX)
    var tax: Float,

    @ColumnInfo(name = CASH)
    var isCash: Boolean,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem, EntityData {

    fun isNewPayment() = id == ID_ADD

    fun asNewPayment() = this.copy(
        id = UUID.randomUUID().toString()
    )

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

        fun createNewPayment(
            name: String,
            desc: String,
            isCash: Boolean
        ) = Payment(
            id = ID_ADD,
            name = name,
            desc = desc,
            tax = 0f,
            isCash = isCash,
            isActive = true,
            isUploaded = false
        )

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
