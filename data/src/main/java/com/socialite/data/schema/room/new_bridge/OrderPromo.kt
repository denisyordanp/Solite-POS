package com.socialite.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.schema.response.OrderPromoResponse
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Promo

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

    @ColumnInfo(name = AppDatabase.UPLOAD)
    val isUpload: Boolean
) : EntityData {

    fun toResponse(): OrderPromoResponse {
        return OrderPromoResponse(
            id = id,
            order = order,
            promo = promo,
            totalPromo = totalPromo.toInt(),
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_order_promo"
        const val PROMO = "total_promo"

        const val DB_NAME = "new_order_promo"
    }
}
