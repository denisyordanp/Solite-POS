package com.socialite.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.OrderPaymentResponse
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Payment

@Entity(
    tableName = OrderPayment.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.ID],
            childColumns = [Order.ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Payment::class,
            parentColumns = [Payment.ID],
            childColumns = [Payment.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [OrderPayment.ID]),
        Index(value = [Order.ID]),
        Index(value = [Payment.ID])
    ]
)
data class OrderPayment(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = Order.ID)
    val order: String,

    @ColumnInfo(name = Payment.ID)
    val payment: String,

    @ColumnInfo(name = PAY)
    val pay: Long,

    @ColumnInfo(name = UPLOAD)
    val isUpload: Boolean
) : EntityData {

    fun toResponse(): OrderPaymentResponse {
        return OrderPaymentResponse(
            id = id,
            pay = pay.toInt(),
            order = order,
            payment = payment,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_order_payment"
        const val PAY = "pay"

        const val DB_NAME = "new_order_payment"
    }
}
