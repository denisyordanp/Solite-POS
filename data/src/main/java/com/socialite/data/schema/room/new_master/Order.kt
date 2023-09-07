package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.OrderResponse
import com.socialite.data.schema.room.EntityData

@Entity(
    tableName = Order.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = [Customer.ID],
            childColumns = [Customer.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [Customer.ID]),
        Index(value = [Order.ID])
    ]
)
data class Order(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = NO)
    val orderNo: String,

    @ColumnInfo(name = Customer.ID)
    val customer: String,

    @ColumnInfo(name = ORDER_DATE)
    val orderTime: String,

    @ColumnInfo(name = TAKE_AWAY)
    val isTakeAway: Boolean,

    @ColumnInfo(name = STATUS)
    val status: Int,

    @ColumnInfo(name = Store.ID)
    val store: String,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

    fun toResponse(): OrderResponse {
        return OrderResponse(
            id = id,
            orderNo = orderNo,
            customer = customer,
            orderTime = orderTime,
            status = status,
            store = store,
            isTakeAway = isTakeAway,
            isUploaded = true
        )
    }

    companion object {

        const val ORDER_DATE = "order_date"
        const val TAKE_AWAY = "take_away"
        const val STATUS = "status"
        const val NO = "order_no"
        const val ID = "order_id"

        const val DB_NAME = "new_order"
    }
}
