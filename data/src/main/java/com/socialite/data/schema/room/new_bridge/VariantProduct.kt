package com.socialite.data.schema.room.new_bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.VariantProductResponse
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Product
import com.socialite.data.schema.room.new_master.Variant
import com.socialite.data.schema.room.new_master.VariantOption

@Entity(
    tableName = VariantProduct.DB_NAME,
    foreignKeys = [
        ForeignKey(
            entity = VariantOption::class,
            parentColumns = [VariantOption.ID],
            childColumns = [VariantOption.ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Variant::class,
            parentColumns = [Variant.ID],
            childColumns = [Variant.ID],
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
        Index(value = [VariantProduct.ID]),
        Index(value = [Variant.ID]),
        Index(value = [VariantOption.ID]),
        Index(value = [Product.ID])
    ]
)
data class VariantProduct(
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    override val id: String,

    @ColumnInfo(name = Variant.ID)
    val variant: String,

    @ColumnInfo(name = VariantOption.ID)
    val variantOption: String,

    @ColumnInfo(name = Product.ID)
    val product: String,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean,

    @ColumnInfo(name = OrderDetail.DELETED, defaultValue = "0")
    val isDeleted: Boolean
) : EntityData {

    fun toResponse(): VariantProductResponse {
        return VariantProductResponse(
            id = id,
            product = product,
            variantOption = variantOption,
            variant = variant,
            isUploaded = true
        )
    }

    companion object {
        const val ID = "id_variant_product"
        const val DELETED = "deleted"

        const val DB_NAME = "new_variant_product"
    }
}
