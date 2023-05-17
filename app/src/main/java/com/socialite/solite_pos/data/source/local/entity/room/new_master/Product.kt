package com.socialite.solite_pos.data.source.local.entity.room.new_master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
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
            name = name,
            desc = desc,
            price = price,
            category = category,
            image = "",
            isActive = true
        )
    }

    @Ignore
    constructor(
        id: String,
        name: String,
        category: String,
        image: String,
        desc: String,
        price: Long,
        isActive: Boolean
    ) : this(
        id,
        name,
        category,
        image,
        desc,
        price,
        isActive,
        false
    )

    @Ignore
    constructor(
        name: String,
        category: String,
        image: String,
        desc: String,
        price: Long,
        isActive: Boolean
    ) : this(
        UUID.randomUUID().toString(),
        name,
        category,
        image,
        desc,
        price,
        isActive,
        false
    )
}
