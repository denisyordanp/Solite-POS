package com.socialite.solite_pos.view.main.opening

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import java.lang.IllegalArgumentException

class OpeningActivity : SocialiteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		val _binding = ActivityOpeningBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		try {
			_binding.tvOpeningVersion.text.apply {
				packageManager.getPackageInfo(packageName, 0).versionName
			}
		} catch (e: IllegalArgumentException) {
			e.printStackTrace()
		}

		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		Handler(Looper.getMainLooper()).postDelayed({toLogin()}, 2000)


    }

	private fun toLogin(){
		finish()
		startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
	}
}
