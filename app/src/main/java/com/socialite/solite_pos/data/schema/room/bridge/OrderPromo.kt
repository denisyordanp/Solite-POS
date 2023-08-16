package com.socialite.solite_pos.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.REPLACED_UUID
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.room.master.Order
import com.socialite.solite_pos.data.schema.room.master.Promo

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

        @ColumnInfo(name = REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = Order.NO)
        var orderNO: String,

        @ColumnInfo(name = Promo.ID)
        var idPromo: Long,

        @ColumnInfo(name = PROMO)
        var totalPromo: Long,

        @ColumnInfo(name = UPLOAD)
        var isUpload: Boolean
) {

    companion object {
        const val ID = "id_order_promo"
        const val PROMO = "total_promo"

        const val DB_NAME = "order_promo"
    }
}
