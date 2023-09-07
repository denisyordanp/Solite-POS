package com.socialite.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.room.master.VariantOption
import java.io.Serializable

@Entity(
        tableName = OrderProductVariant.DB_NAME,
        foreignKeys = [
            ForeignKey(
                    entity = OrderDetail::class,
                    parentColumns = [OrderDetail.ID],
                    childColumns = [OrderDetail.ID],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = VariantOption::class,
                    parentColumns = [VariantOption.ID],
                    childColumns = [VariantOption.ID],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = [OrderProductVariant.ID]),
            Index(value = [OrderDetail.ID]),
            Index(value = [VariantOption.ID])
        ]
)
data class OrderProductVariant(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = OrderDetail.ID)
        var idOrderDetail: Long,

        @ColumnInfo(name = VariantOption.ID)
        var idVariantOption: Long,

        @ColumnInfo(name = UPLOAD)
        var isUpload: Boolean
) : Serializable {
    companion object {
        const val ID = "id_order_product_variant"

        const val DB_NAME = "order_product_variant"
    }

}
