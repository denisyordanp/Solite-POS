package com.socialite.solite_pos.data.source.local.entity.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import java.util.UUID

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
    var id: String,

    @ColumnInfo(name = Order.ID)
    var order: String,

    @ColumnInfo(name = Promo.ID)
    var promo: String,

    @ColumnInfo(name = PROMO)
    var totalPromo: Long,

    @ColumnInfo(name = AppDatabase.UPLOAD)
    var isUpload: Boolean
) {

    companion object {
        const val ID = "id_order_promo"
        const val PROMO = "total_promo"

        const val DB_NAME = "new_order_promo"

        fun newPromo(
            orderNo: String,
            promo: String,
            totalPromo: Long
        ) = OrderPromo(
            id = UUID.randomUUID().toString(),
            order = orderNo,
            promo = promo,
            totalPromo = totalPromo,
            isUpload = false
        )
    }
}
