package com.socialite.schema.database.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.master.Order
import com.socialite.schema.database.master.Payment
import java.io.Serializable

@Entity(
    tableName = OrderPayment.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.NO],
            childColumns = [Order.NO],
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
        Index(value = [Order.NO]),
        Index(value = [Payment.ID])
    ]
)
data class OrderPayment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
    val new_id: String,

    @ColumnInfo(name = Order.NO)
    var orderNO: String,

    @ColumnInfo(name = Payment.ID)
    var idPayment: Long,

    @ColumnInfo(name = PAY)
    var pay: Long,

    @ColumnInfo(name = UPLOAD)
    var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_payment"
        const val PAY = "pay"
        const val UPLOAD = "upload"
        const val REPLACED_UUID = "replaced_uuid"

        const val DB_NAME = "order_payment"
    }
}
