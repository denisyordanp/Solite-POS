package com.socialite.schema.database.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.schema.database.master.VariantOption
import java.io.Serializable

@Entity(
    tableName = OrderMixProductVariant.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = OrderProductVariantMix::class,
            parentColumns = [OrderProductVariantMix.ID],
            childColumns = [OrderProductVariantMix.ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VariantOption::class,
            parentColumns = [VariantOption.ID],
            childColumns = [VariantOption.ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [OrderMixProductVariant.ID]),
        Index(value = [OrderProductVariantMix.ID]),
        Index(value = [VariantOption.ID])
    ]
)
data class OrderMixProductVariant(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = OrderProductVariantMix.ID)
    var idOrderProductMixVariant: Long,

    @ColumnInfo(name = VariantOption.ID)
    var idVariantOption: Long,

    @ColumnInfo(name = UPLOAD)
    var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_mix_product_variant"
        const val UPLOAD = "upload"

        const val DB_NAME = "order_mix_product_variant"
    }

}
