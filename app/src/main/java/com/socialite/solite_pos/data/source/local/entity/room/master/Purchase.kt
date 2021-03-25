package com.socialite.solite_pos.data.source.local.entity.room.master

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.UPLOAD
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentTime
import com.socialite.solite_pos.utils.preference.PurchasePref
import com.socialite.solite_pos.utils.tools.RemoteUtils
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

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
): Serializable {

	companion object: RemoteUtils<Purchase> {

        const val PURCHASE_DATE = "purchase_date"
        const val NO = "purchase_no"

        const val DB_NAME = "purchase"

        private var purchasePref: PurchasePref? = null

        fun purchaseNo(context: Context): String {
            purchasePref = PurchasePref(context)
            return id
        }

        private val id: String
            get() {
                if (date != savedDate) {
                    saveDate()
                }
                return "$savedDate${setNumber(purchasePref!!.purchaseCount)}"
            }

		private fun setNumber(i: Int): String{
			var str = i.toString()
			when(str.length){
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
                val date = purchasePref!!.purchaseDate
                return date ?: ""
            }

		private fun saveDate() {
            purchasePref!!.purchaseDate = date
            reset()
        }

		fun add(context: Context) {
            if (purchasePref == null) {
                purchasePref = PurchasePref(context)
            }
            purchasePref!!.purchaseCount = purchasePref!!.purchaseCount + 1
        }

		private fun reset() {
            purchasePref!!.purchaseCount = 1
        }

		override fun toHashMap(data: Purchase): HashMap<String, Any?> {
			return hashMapOf(
					NO to data.purchaseNo,
					Supplier.ID to data.idSupplier,
					PURCHASE_DATE to data.purchaseTime,
					UPLOAD to data.isUploaded
			)
		}

		override fun toListClass(result: QuerySnapshot): List<Purchase> {
			val array: ArrayList<Purchase> = ArrayList()
			for (document in result){
				val purchase = Purchase(
                        document.data[NO] as String,
                        document.data[Supplier.ID] as Long,
                        document.data[PURCHASE_DATE] as String,
                        document.data[UPLOAD] as Boolean
                )
                array.add(purchase)
            }
            return array
        }
    }

    constructor(context: Context, idSupplier: Long) : this(purchaseNo(context), idSupplier, currentDateTime, false)

}
