package com.socialite.solite_pos.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.socialite.solite_pos.R

open class SoliteActivity : AppCompatActivity() {

    private var isBackNoticed: Boolean = false

    override fun onBackPressed() {
        if (isTaskRoot && !isBackNoticed) {
            Toast.makeText(this, getString(R.string.touch_once_more_to_close_the_app), Toast.LENGTH_LONG)
                .show()
            isBackNoticed = true
        } else {
            super.onBackPressed()
        }
    }

    private var messageReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            // TODO: show message
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
