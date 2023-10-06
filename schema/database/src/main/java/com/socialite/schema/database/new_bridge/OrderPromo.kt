package com.socialite.schema.database.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.new_master.Order
import com.socialite.schema.database.new_master.Promo

@Entity(
    tableName = OrderPromo.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.ID],
            childColumns = [Order.ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Promo::class,
            parentColumns = [Promo.ID],
            childColumns = [Promo.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [OrderPromo.ID]),
        Index(value = [Order.ID]),
        Index(value = [Promo.ID])
    ]
)
data class OrderPromo(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = Order.ID)
    val order: String,

    @ColumnInfo(name = Promo.ID)
    val promo: String,

    @ColumnInfo(name = PROMO)
    val totalPromo: Long,

    @ColumnInfo(name = UPLOAD)
    val isUpload: Boolean
) : EntityData {

    companion object {
        const val ID = "id_order_promo"
        const val PROMO = "total_promo"
        const val UPLOAD = "upload"

        const val DB_NAME = "new_order_promo"
    }
}
