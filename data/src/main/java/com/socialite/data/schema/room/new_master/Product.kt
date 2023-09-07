package com.socialite.data.schema.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.data.database.AppDatabase.Companion.UPLOAD
import com.socialite.data.schema.response.ProductResponse
import com.socialite.data.schema.room.EntityData

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
    override val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = Category.ID)
    val category: String,

    @ColumnInfo(name = IMAGE)
    val image: String,

    @ColumnInfo(name = DESC)
    val desc: String,

    @ColumnInfo(name = PRICE)
    val price: Long,

    @ColumnInfo(name = STATUS)
    val isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    val isUploaded: Boolean
) : EntityData {

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
    }
}
