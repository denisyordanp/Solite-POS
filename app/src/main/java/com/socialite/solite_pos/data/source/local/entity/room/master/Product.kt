package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import java.io.Serializable

@Entity(
        tableName = Product.DB_NAME,
        foreignKeys = [ForeignKey(
                entity = Category::class,
                parentColumns = [Category.ID],
                childColumns = [Category.ID],
                onDelete = CASCADE
        )
        ],
        indices = [
            Index(value = [Product.ID]),
            Index(value = [Category.ID])
        ]
)
data class Product(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        var id: Long,

        @ColumnInfo(name = AppDatabase.REPLACED_UUID, defaultValue = "")
        val new_id: String,

        @ColumnInfo(name = NAME)
        var name: String,

        @ColumnInfo(name = Category.ID)
        var category: Long,

        @ColumnInfo(name = IMAGE)
        var image: String,

        @ColumnInfo(name = DESC)
        var desc: String,

        @ColumnInfo(name = SELL_PRICE)
        var sellPrice: Long,

        @ColumnInfo(name = BUY_PRICE)
        var buyPrice: Long,

        @ColumnInfo(name = PORTION)
        var portion: Int,

        @ColumnInfo(name = STOCK)
        var stock: Long,

        @ColumnInfo(name = MIX)
        var isMix: Boolean,

        @ColumnInfo(name = STATUS)
        var isActive: Boolean,

        @ColumnInfo(name = UPLOAD)
        var isUploaded: Boolean
) : Serializable {
    companion object {
        const val SELL_PRICE = "sell_price"
        const val BUY_PRICE = "buy_price"
        const val PORTION = "portion"
        const val ID = "id_product"
        const val STATUS = "status"
        const val STOCK = "stock"
        const val IMAGE = "image"
        const val NAME = "name"
        const val DESC = "desc"
        const val MIX = "mix"

        const val DB_NAME = "product"
    }
}
