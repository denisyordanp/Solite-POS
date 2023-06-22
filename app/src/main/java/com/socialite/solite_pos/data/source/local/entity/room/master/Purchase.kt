package com.socialite.solite_pos.data.source.local.entity.room.master

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.data.source.preference.SettingPreferences
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentTime
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(
    tableName = Purchase.DB_NAME,
    primaryKeys = [Purchase.NO],
    indices = [
        Index(value = [Purchase.NO])
    ]
)
data class Purchase(
    @ColumnInfo(name = NO)
    var purchaseNo: String,

    @ColumnInfo(name = Supplier.ID)
    var idSupplier: Long,

    @ColumnInfo(name = PURCHASE_DATE)
    var purchaseTime: String,

    @ColumnInfo(name = UPLOAD)
    var isUploaded: Boolean
) : Serializable {

    companion object {

        const val PURCHASE_DATE = "purchase_date"
        const val NO = "purchase_no"

        const val DB_NAME = "purchase"

        private var settingPreferences: SettingPreferences? = null

        fun purchaseNo(context: Context): String {
            settingPreferences = SettingPreferences(context)
            return id
        }

        private val id: String
            get() {
                if (date != savedDate) {
                    saveDate()
                }
                return "$savedDate${setNumber(settingPreferences!!.purchaseCount)}"
            }

        private fun setNumber(i: Int): String {
            var str = i.toString()
            when (str.length) {
                1 -> str = "00$i"
                2 -> str = "0$i"
            }
            return str
        }

        private val date: String
            get() {
                val fd = SimpleDateFormat("ddMMyy", Locale.getDefault())
                return fd.format(currentTime)
            }

        private val savedDate: String
            get() {
                val date = settingPreferences!!.purchaseDate
                return date ?: ""
            }

        private fun saveDate() {
            settingPreferences!!.purchaseDate = date
            reset()
        }

        fun add(context: Context) {
            if (settingPreferences == null) {
                settingPreferences = SettingPreferences(context)
            }
            settingPreferences!!.purchaseCount = settingPreferences!!.purchaseCount + 1
        }

        private fun reset() {
            settingPreferences!!.purchaseCount = 1
        }
    }

}
