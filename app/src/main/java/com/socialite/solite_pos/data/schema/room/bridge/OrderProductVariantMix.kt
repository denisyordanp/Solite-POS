package com.socialite.solite_pos.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.schema.room.master.Product
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
    tableName = OrderProductVariantMix.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = OrderDetail::class,
            parentColumns = [OrderDetail.ID],
            childColumns = [OrderDetail.ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = [Product.ID],
            childColumns = [Product.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [OrderProductVariantMix.ID]),
        Index(value = [OrderDetail.ID]),
        Index(value = [Product.ID])
    ]
)
data class OrderProductVariantMix(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = OrderDetail.ID)
    var idOrderDetail: Long,

    @ColumnInfo(name = Product.ID)
    var idProduct: Long,

    @ColumnInfo(name = AMOUNT)
    var amount: Int,

    @ColumnInfo(name = UPLOAD)
    var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_product_variant_mix"
        const val AMOUNT = "amount"

        const val DB_NAME = "order_product_variant_mix"
    }
}
