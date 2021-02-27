package com.sosialite.solite_pos.utils.config

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.viewmodelFactory.ViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainConfig {
	companion object{

		private const val dbFormat = "yyyy-MM-dd HH:mm:ss"
		const val ldFormat = "dd MMMM yyyy HH:mm"
		const val sdFormat = "EEE, dd MMMM yyyy"

		fun setDialogFragment(w: Window?){
			w?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
			w?.requestFeature(Window.FEATURE_NO_TITLE)
		}

		fun productOrderIndex(array: ArrayList<ProductOrderDetail>, detail: ProductOrderDetail?): Int?{
			for ((i, v) in array.withIndex()){
				if (v.product != null){
					if (v.product == detail?.product && v.variants == detail?.variants){
						return i
					}
				}
			}
			return null
		}

		fun productPurchaseIndex(array: ArrayList<PurchaseProductWithProduct>, detail: PurchaseProductWithProduct?): Int?{
			for ((i, v) in array.withIndex()){
				if (v.product != null){
					if (v.product == detail?.product){
						return i
					}
				}
			}
			return null
		}

		val currentTime: Date
		get() = Calendar.getInstance().time

		val currentDate: String
		get() {
			return SimpleDateFormat(dbFormat, Locale.getDefault()).format(currentTime)
		}

		fun toRupiah(amount: Long?): String{
			if (amount != null){
				return "Rp. ${thousand(amount)}"
			}
			return ""
		}

		fun thousand(amount: Long?): String{
			return NumberFormat.getNumberInstance(Locale.getDefault()).format(amount)
		}

		fun strToDate(date: String?): Date{
			return if (date.isNullOrEmpty()){
				Date()
			}else{
				val db = SimpleDateFormat(dbFormat, Locale.getDefault())
				return db.parse(date)!!
			}
		}

		fun dateFormat(date: String?, format: String): String {
			return if (date != null && date.isNotEmpty()) {
				val db = SimpleDateFormat(dbFormat, Locale.getDefault())
				val ld = SimpleDateFormat(format, Locale.getDefault())
				val d = db.parse(date)
				if (d != null) ld.format(d) else ""
			}else{
				""
			}
		}

		fun getViewModel(context: FragmentActivity): MainViewModel{
			return ViewModelProvider(context, ViewModelFactory.getInstance(context.applicationContext)).get(MainViewModel::class.java)
		}
	}
}
