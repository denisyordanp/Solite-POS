package com.socialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity

class OpeningActivity : SocialiteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityOpeningBinding.inflate(layoutInflater).root)
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		Handler(Looper.getMainLooper()).postDelayed({toMain()}, 2000)
    }

	private fun toMain(){
		finish()
		startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
	}
}
