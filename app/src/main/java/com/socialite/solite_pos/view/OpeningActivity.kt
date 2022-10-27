package com.socialite.solite_pos.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity

class OpeningActivity : SocialiteActivity() {

//	private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityOpeningBinding.inflate(layoutInflater)
		setContentView(binding.root)

//		auth = Firebase.auth

		try {
			binding.tvOpeningVersion.text.apply {
				packageManager.getPackageInfo(packageName, 0).versionName
			}
		} catch (e: IllegalArgumentException) {
			e.printStackTrace()
		}

		Handler(Looper.getMainLooper()).postDelayed({checkUser()}, 2000)

    }

	private fun checkUser() {
//		toMain()
		toMain()
//		if (auth.currentUser != null) {
//			toMain()
//		} else {
//			toLogin()
//		}
	}

	private fun toMain() {
		val intent = Intent(this, OrderCustomerActivity::class.java)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
			.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		startActivity(intent)
	}

//	private fun toLogin() {
//		finish()
//		startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
//	}
}
