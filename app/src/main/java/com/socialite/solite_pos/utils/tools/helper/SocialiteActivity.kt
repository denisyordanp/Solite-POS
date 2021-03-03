package com.socialite.solite_pos.utils.tools.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

open class SocialiteActivity : AppCompatActivity() {

	private var messageReceiver = object : BroadcastReceiver(){
		override fun onReceive(context: Context?, intent: Intent?) {
			ShowMessage(supportFragmentManager)
				.setName(intent?.getStringExtra(NAME))
				.show()
		}
	}

	companion object{
		var isActive: Boolean = false
		const val BROADCAST_KEY = "alert"
		const val NAME = "name"
	}

	override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {}

	override fun onResume() {
		super.onResume()
		isActive = true
		LocalBroadcastManager.getInstance(this).registerReceiver(
			messageReceiver, IntentFilter(BROADCAST_KEY)
		)
	}

	override fun onPause() {
		super.onPause()
		isActive = false
		LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
	}
}
