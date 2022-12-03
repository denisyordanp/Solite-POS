package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.view.ui.DropdownItem
import java.io.Serializable

@Entity(
    tableName = Promo.DB_NAME,
    indices = [
        Index(value = [Promo.ID])
    ]
)
data class Promo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long,

    @ColumnInfo(name = NAME)
    override var name: String,

    @ColumnInfo(name = DESC)
    var desc: String,

    @ColumnInfo(name = CASH)
    var isCash: Boolean,

    @ColumnInfo(name = STATUS)
    var isActive: Boolean,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable, DropdownItem {

    fun isNewPromo() = id == 0L

    companion object {
        const val ID = "id_promo"
        const val STATUS = "status"
        const val NAME = "name"
        const val DESC = "desc"
        const val CASH = "cash"

        const val DB_NAME = "promo"

        fun createNewPromo(
            name: String,
            desc: String,
            isCash: Boolean
        ) = Promo(
            id = 0L,
            name = name,
            desc = desc,
            isCash = isCash,
            isActive = true,
            isUploaded = false
        )
    }
}
