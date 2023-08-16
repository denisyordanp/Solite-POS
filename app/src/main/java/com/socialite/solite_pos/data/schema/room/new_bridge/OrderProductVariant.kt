package com.socialite.solite_pos.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.schema.room.EntityData
import com.socialite.solite_pos.data.schema.room.new_master.VariantOption
import com.socialite.solite_pos.database.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.schema.response.OrderProductVariantResponse
import java.io.Serializable
import java.util.UUID

@Entity(
    tableName = OrderProductVariant.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = OrderDetail::class,
            parentColumns = [OrderDetail.ID],
            childColumns = [OrderDetail.ID],
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
        Index(value = [OrderProductVariant.ID]),
        Index(value = [OrderDetail.ID]),
        Index(value = [VariantOption.ID])
    ]
)
data class OrderProductVariant(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = OrderDetail.ID)
    val orderDetail: String,

    @ColumnInfo(name = VariantOption.ID)
    val variantOption: String,

    @ColumnInfo(name = UPLOAD)
    val isUpload: Boolean,

    @ColumnInfo(name = DELETED, defaultValue = "0")
    val isDeleted: Boolean
) : Serializable, EntityData {

    fun toResponse(): OrderProductVariantResponse {
        return OrderProductVariantResponse(
                id = id,
                orderDetail = orderDetail,
                variantOption = variantOption,
                isUploaded = true
        )
    }

    companion object {
        const val ID = "id_order_product_variant"
        const val DELETED = "deleted"

        const val DB_NAME = "new_order_product_variant"

        fun createNewOrderVariant(
            orderDetail: String, variantOption: String
        ) = OrderProductVariant(
            id = UUID.randomUUID().toString(),
            orderDetail = orderDetail,
            variantOption = variantOption,
            isUpload = false,
                isDeleted = false
        )
    }
}
