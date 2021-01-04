package com.sosialite.solite_pos.utils.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity

class OrderListBroadcast(private var context: Context) {

	companion object{
		const val PRODUCT = "product"
		const val BC_KEY_PRODUCT = "bc_key_product"
	}

	private var deleteReceiver: BroadcastReceiver? = null

	fun sendBroadcastProduct(code: Int?){
		val intent = Intent(BC_KEY_PRODUCT)
		intent.putExtra(PRODUCT, code)
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
	}

	fun setReceiver(callback: ((Int?) -> Unit)){
		deleteReceiver = object : BroadcastReceiver(){
			override fun onReceive(context: Context?, intent: Intent?) {
				val code = intent?.getIntExtra(PRODUCT, 0)
				callback.invoke(code)
			}
		}
		LocalBroadcastManager.getInstance(context).registerReceiver(
				deleteReceiver!!, IntentFilter(BC_KEY_PRODUCT)
		)
	}

	fun removeReceiver(){
		if (deleteReceiver != null){
			LocalBroadcastManager.getInstance(context).unregisterReceiver(deleteReceiver!!)
		}
	}
}
