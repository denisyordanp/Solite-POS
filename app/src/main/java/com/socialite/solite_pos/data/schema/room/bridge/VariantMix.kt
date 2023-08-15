package com.socialite.solite_pos.data.schema.room.bridge

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.schema.room.master.Product
import com.socialite.solite_pos.data.schema.room.master.Variant
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
    tableName = VariantMix.DB_NAME,
    foreignKeys = [
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
        Index(value = [VariantMix.ID]),
        Index(value = [Variant.ID]),
        Index(value = [Product.ID])
    ]
)
data class VariantMix(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = Variant.ID)
    var idVariant: Long,

    @ColumnInfo(name = Product.ID)
    var idProduct: Long,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {
    companion object {
        const val ID = "id_variant_mix"

        const val DB_NAME = "variant_mix"
    }
}
