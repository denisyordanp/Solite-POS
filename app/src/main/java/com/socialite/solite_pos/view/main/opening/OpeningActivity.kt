package com.socialite.solite_pos.view.main.opening

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity

class OpeningActivity : SocialiteActivity() {

	private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityOpeningBinding.inflate(layoutInflater)
		setContentView(binding.root)

		auth = Firebase.auth

		try {
			binding.tvOpeningVersion.text.apply {
				packageManager.getPackageInfo(packageName, 0).versionName
			}
		} catch (e: IllegalArgumentException) {
			e.printStackTrace()
		}

		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		Handler(Looper.getMainLooper()).postDelayed({checkUser()}, 2000)

    }

	private fun checkUser() {
		if (auth.currentUser != null) {
			toMain()
		} else {
			toLogin()
		}
	}

	private fun toMain() {
		finish()
		startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
	}

	private fun toLogin() {
		finish()
		startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
	}
}
