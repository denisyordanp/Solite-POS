package com.socialite.solite_pos.view.main.menu.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.databinding.FragmentSettingBinding
import com.socialite.solite_pos.utils.preference.SettingPref
import com.socialite.solite_pos.utils.preference.UserPref
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import com.socialite.solite_pos.view.main.opening.LoginActivity

class SettingFragment : Fragment() {

    private lateinit var _binding: FragmentSettingBinding
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

            userPref = UserPref(activity!!)
            auth = Firebase.auth

            setCookTime()

            _binding.btnStCookAdd.setOnClickListener {
                SettingPref(activity!!).cookTime = SettingPref(activity!!).cookTime + 1
                setCookTime()
            }
            _binding.btnStCookMin.setOnClickListener {
                SettingPref(activity!!).cookTime = SettingPref(activity!!).cookTime - 1
                setCookTime()
            }

            _binding.btnStBluetooth.setOnClickListener {
                startActivity(Intent(activity, BluetoothDeviceListActivity::class.java))
            }
            _binding.btnMtLogout.setOnClickListener {
                showLogoutMessage()
            }
        }
    }

    private fun showLogoutMessage() {
        MessageBottom(activity!!.supportFragmentManager)
                .setMessage("Anda akan keluar, lanjutkan?")
                .setPositiveListener(activity!!.getString(android.R.string.ok)) {
                    logout()
                }
                .setNegativeListener(activity!!.getString(android.R.string.cancel), null)
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

    private fun setCookTime() {
        val cook = SettingPref(activity!!).cookTime
        val minute = "${cook}m"
        _binding.tvStCookTime.text = minute
        if (cook <= MIN_COOK) {
            _binding.btnStCookMin.visibility = View.INVISIBLE
        } else {
            _binding.btnStCookMin.visibility = View.VISIBLE
		}
	}
}
