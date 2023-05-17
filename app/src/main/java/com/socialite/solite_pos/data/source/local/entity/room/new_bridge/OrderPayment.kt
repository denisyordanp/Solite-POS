package com.socialite.solite_pos.data.source.local.entity.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable
import java.util.UUID

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
    var id: String,

    @ColumnInfo(name = Order.ID)
    var order: String,

    @ColumnInfo(name = Payment.ID)
    var payment: String,

    @ColumnInfo(name = PAY)
    var pay: Long,

    @ColumnInfo(name = UPLOAD)
    var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_payment"
        const val PAY = "pay"

        const val DB_NAME = "new_order_payment"
    }

    @Ignore
    constructor(order: String, payment: String, pay: Long) : this(
        id = UUID.randomUUID().toString(),
        order = order,
        payment = payment,
        pay = pay,
        isUpload = false
    )

    fun inReturn(total: Long): Long {
        return pay - total
    }
}
