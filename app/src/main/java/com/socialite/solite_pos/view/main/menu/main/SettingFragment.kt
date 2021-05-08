package com.socialite.solite_pos.view.main.menu.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.R
import com.socialite.solite_pos.databinding.FragmentSettingBinding
import com.socialite.solite_pos.utils.preference.SettingPref
import com.socialite.solite_pos.utils.preference.UserPref
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import com.socialite.solite_pos.view.main.opening.LoginActivity

class SettingFragment : Fragment() {

    private lateinit var _binding: FragmentSettingBinding
    private lateinit var setting : SettingPref
    private lateinit var userPref: UserPref
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val MIN_COOK = 1
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {

		    setting = SettingPref(activity!!)
            userPref = UserPref(activity!!)
            auth = Firebase.auth

            setCookTime()

            _binding.btnStCookAdd.setOnClickListener { addCookTime() }
            _binding.btnStCookMin.setOnClickListener { minCookTIme() }

            _binding.btnStBluetooth.setOnClickListener {
                startActivity(Intent(activity, BluetoothDeviceListActivity::class.java))
            }
            _binding.btnMtLogout.setOnClickListener { showLogoutMessage() }
            _binding.btnStResetBluetooth.setOnClickListener { resetSavedPrinter() }
        }
    }

    private fun showLogoutMessage() {
        MessageBottom(activity!!.supportFragmentManager)
            .setMessageImage(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_alert_message, null))
            .setMessage("Anda akan keluar, lanjutkan?")
            .setPositiveListener(activity!!.getString(android.R.string.ok)) {
                logout()
            }
            .setNegativeListener(activity!!.getString(android.R.string.cancel)) {
                it?.dismiss()
            }
            .show()
    }

    private fun logout() {
        auth.signOut()
        userPref.userAuthority = null
        val intent = Intent(activity!!, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.finish()
    }

    private fun addCookTime() {
        setting.cookTime = setting.cookTime + 1
        setCookTime()
    }

    private fun minCookTIme() {
        setting.cookTime = setting.cookTime - 1
        setCookTime()
    }

    private fun setCookTime() {
        val cook = setting.cookTime
        val minute = "${cook}m"
        _binding.tvStCookTime.text = minute
        if (cook <= MIN_COOK) {
            _binding.btnStCookMin.visibility = View.INVISIBLE
        } else {
            _binding.btnStCookMin.visibility = View.VISIBLE
		}
	}

    private fun resetSavedPrinter() {
        setting.printerDevice = ""
    }
}
