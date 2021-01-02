package com.sosialite.solite_pos.utils.config

import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainConfig {
	companion object{

		fun productIndex(array: ArrayList<DetailOrder>, product: Product?): Int?{
			for ((i, v) in array.withIndex()){
				if (v.product != null){
					if (v.product!! == product){
						return i
					}
				}
			}
			return null
		}

		val currentTime: Date
		get() = Calendar.getInstance().time

		fun toRupiah(amount: Int?): String{
			if (amount != null){
				return "Rp. ${thousand(amount)}"
			}
			return ""
		}

		fun thousand(amount: Int?): String{
			return NumberFormat.getNumberInstance(Locale.getDefault()).format(amount)
		}

		fun dateFormat(date: String?): String? {
			if (date != null && date.isNotEmpty()) {
				val db = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
				val ld = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
				val d: Date?
				d = try {
					db.parse(date)
				} catch (e: ParseException) {
					e.printStackTrace()
					return ""
				}
				return if (d != null) ld.format(d) else ""
			}
			return ""
		}
	}
}
