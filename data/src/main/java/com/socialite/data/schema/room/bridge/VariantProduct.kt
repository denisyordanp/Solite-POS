package com.socialite.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.room.master.Product
import com.socialite.data.schema.room.master.Variant
import com.socialite.data.schema.room.master.VariantOption
import java.io.Serializable

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

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = Variant.ID)
        var idVariant: Long,

        @ColumnInfo(name = VariantOption.ID)
        var idVariantOption: Long,

        @ColumnInfo(name = Product.ID)
        var idProduct: Long,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {
    companion object {
        const val ID = "id_variant_product"

        const val DB_NAME = "variant_product"
    }
}
