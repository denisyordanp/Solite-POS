package com.socialite.solite_pos.data.source.local.entity.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import java.util.UUID
import com.socialite.solite_pos.data.source.remote.response.entity.OrderPromoResponse

@Entity(
    tableName = OrderPromo.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.NO],
            childColumns = [Order.NO],
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
        Index(value = [Order.NO]),
        Index(value = [Promo.ID])
    ]
)
data class OrderPromo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
    val new_id: String,

    @ColumnInfo(name = Order.NO)
    var orderNO: String,

    @ColumnInfo(name = Promo.ID)
    var idPromo: Long,

    @ColumnInfo(name = PROMO)
    var totalPromo: Long,

    @ColumnInfo(name = AppDatabase.UPLOAD)
    var isUpload: Boolean
) {

    companion object {
        const val ID = "id_order_promo"
        const val PROMO = "total_promo"

        const val DB_NAME = "order_promo"

        fun newPromo(
            orderNo: String,
            promo: Promo,
            totalPromo: Long
        ) = OrderPromo(
            id = 0L,
            UUID.randomUUID().toString(),
            orderNO = orderNo,
            idPromo = promo.id,
            totalPromo = totalPromo,
            isUpload = false
        )
    }

    fun toResponse(): OrderPromoResponse {
        return OrderPromoResponse(
            id = id.toString(),
            order = orderNO,
            promo = idPromo.toString(),
            totalPromo = totalPromo.toInt(),
            isUploaded = true
        )
    }
}
