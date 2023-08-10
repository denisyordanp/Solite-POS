package com.socialite.solite_pos.utils.tools

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.socialite.solite_pos.R
import com.socialite.solite_pos.view.screens.opening.OpeningActivity

class NotificationUtils {
	private lateinit var context: Context
	private val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

	companion object{

		private const val CHANNEL_ID = "cook_time"
		private const val CHANNEL_NAME = "Cook time alert"
		private const val NOTIFICATION_ID = 101

		fun setNotification(title: String?, context: Context?){
			if (context != null){
				NotificationUtils().show(
					CHANNEL_ID,
					CHANNEL_NAME,
					title ?: "",
					"Pesanan udah siap, yuk angkat sekarang",
					NOTIFICATION_ID,
					Intent(context, OpeningActivity::class.java),
					context
				)
			}
		}
	}

	fun show(
		channelId: String,
		channelName: String,
		title: String,
		message: String,
		notificationId: Int,
		intent: Intent,
		context: Context
	) {
		this.context = context

		val pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
		val builder = createBuilder(channelId, title, message, pendingIntent)

		createNotificationChannel(channelId, channelName)
		if (ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.POST_NOTIFICATIONS
			) != PackageManager.PERMISSION_GRANTED
		) {
			NotificationManagerCompat.from(context).notify(notificationId, builder.build())
		}
	}

	private fun createBuilder(channelId: String, title: String, message: String, pendingIntent: PendingIntent): NotificationCompat.Builder {
		val builder = NotificationCompat.Builder(context, channelId)
		builder.setSmallIcon(R.drawable.ic_warning)
		builder.color = ContextCompat.getColor(context, android.R.color.transparent)
		builder.priority = NotificationCompat.PRIORITY_HIGH
		builder.setContentTitle(title)
		builder.setContentIntent(pendingIntent)
		builder.setContentText(message)
		builder.setAutoCancel(true)
		builder.setSound(uriTone)
		return builder
	}

	private fun createNotificationChannel(channelId: String, channelName: String) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				channelName,
				NotificationManager.IMPORTANCE_HIGH)
			channel.enableLights(true)
			channel.lightColor = Color.YELLOW
			channel.enableVibration(true)
			context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
		}
	}
}
