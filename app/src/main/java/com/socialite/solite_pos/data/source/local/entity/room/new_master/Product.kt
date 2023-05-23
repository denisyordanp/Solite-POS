package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.remote.response.entity.ProductResponse
import java.io.Serializable
import java.util.UUID

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
    @PrimaryKey
    @ColumnInfo(name = ID, defaultValue = "")
    val id: String,

    @ColumnInfo(name = NAME)
    var name: String,

    @ColumnInfo(name = Category.ID)
    var category: String,

    @ColumnInfo(name = IMAGE)
    var image: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = PRICE)
    var price: Long,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {

    fun toResponse(): ProductResponse {
        return ProductResponse(
                id = id,
                name = name,
                desc = desc,
                category = category,
                image = image,
                price = price.toInt(),
                isActive = isActive,
                isUploaded = true
        )
    }
    companion object {
        const val PRICE = "price"
        const val ID = "id_product"
        const val STATUS = "status"
        const val IMAGE = "image"
        const val NAME = "name"
        const val DESC = "desc"

        const val DB_NAME = "new_product"

        fun createNewProduct(
            name: String,
            desc: String,
            price: Long,
            category: String
        ) = Product(
            id = UUID.randomUUID().toString(),
            name = name,
            desc = desc,
            price = price,
            category = category,
            image = "",
            isActive = true,
            isUploaded = false
        )
    }
}
