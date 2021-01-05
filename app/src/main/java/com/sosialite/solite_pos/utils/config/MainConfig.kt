package com.sosialite.solite_pos.utils.config

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.viewmodelFactory.ViewModelFactory
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainConfig {
	companion object{

		fun orderIndex(array: ArrayList<Order>, order: Order): Int?{
			for ((i, v) in array.withIndex()){
				if (order.orderNo == v.orderNo){
					return i
				}
			}
			return null
		}

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

		fun getViewModel(context: FragmentActivity): MainViewModel{
			return ViewModelProvider(context, ViewModelFactory.getInstance(context.applicationContext)).get(MainViewModel::class.java)
		}
	}
}
