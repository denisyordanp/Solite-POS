package com.socialite.solite_pos.utils.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity.Companion.BROADCAST_KEY
import java.util.*

class DoneCookService(private var context: Context?) : BroadcastReceiver() {

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
			}
			sendNotification(name)
		}
	}

	private fun sendNotification(name: String?){
		NotificationUtils.setNotification(name, context)
	}

	private fun sendBroadcastAlert(name: String?, context: Context){
		val intent = Intent(BROADCAST_KEY)
		intent.putExtra(SocialiteActivity.NAME, name)
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
	}

	fun set(order: OrderData) {
		if (context != null){
			val time = order.order.getFinishCook(context!!).time
			val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			val calendar = Calendar.getInstance()
			calendar.time = time
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, getPendingIntent(order.order.orderNo.toInt(), order.customer.name))
		}
	}

	private fun getPendingIntent(id: Int, name: String?): PendingIntent? {
		return PendingIntent.getBroadcast(
			context,
			id,
			Intent(context, DoneCookService::class.java)
				.putExtra(EXTRA_NAME, name),
			PendingIntent.FLAG_UPDATE_CURRENT
		)
	}
}
