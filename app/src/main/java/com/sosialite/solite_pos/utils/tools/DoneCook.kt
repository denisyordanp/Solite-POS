package com.sosialite.solite_pos.utils.tools

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity.Companion.BROADCAST_KEY

class DoneCook(private var context: Context?) : BroadcastReceiver() {

	constructor(): this(null)

	companion object{
		const val EXTRA_NAME = "extra_name"
	}

	override fun onReceive(context: Context?, intent: Intent?) {
		if (context != null){
			this.context = context
			val name = intent?.getStringExtra(EXTRA_NAME)
			if (SocialiteActivity.isActive){
				sendBroadcastAlert(name, context)
			}else{
				sendNotification(name)
			}
		}
	}

	private fun sendNotification(name: String?){
		SendNotification.setNotification(name, context)
	}

	private fun sendBroadcastAlert(name: String?, context: Context){
		val intent = Intent(BROADCAST_KEY)
		intent.putExtra(SocialiteActivity.NAME, name)
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
	}

	fun set(order: Order) {
		if (context != null){
//			val time = order.getFinishCook(context!!)?.time
//			if (time != null){
//				val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//				val calendar = Calendar.getInstance()
//				calendar.time = time
//				TODO("selesaikan")
//				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, getPendingIntent(order.orderNo, order.customer?.name))
//			}
		}
	}

	private fun getPendingIntent(id: Int, name: String?): PendingIntent? {
		return PendingIntent.getBroadcast(
			context,
			id,
			Intent(context, DoneCook::class.java)
				.putExtra(EXTRA_NAME, name),
			0)
	}
}
