package com.socialite.solite_pos.view.main.menu.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.socialite.solite_pos.databinding.FragmentSettingBinding
import com.socialite.solite_pos.utils.config.SettingPref
import com.socialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity

class SettingFragment : Fragment() {

	private lateinit var _binding: FragmentSettingBinding

	companion object {
		private const val MIN_COOK = 1
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSettingBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			setCookTime()

			_binding.btnStCookAdd.setOnClickListener {
				SettingPref(activity!!).cookTime = SettingPref(activity!!).cookTime+1
				setCookTime()
			}
			_binding.btnStCookMin.setOnClickListener {
				SettingPref(activity!!).cookTime = SettingPref(activity!!).cookTime-1
				setCookTime()
			}

			_binding.btnStBluetooth.setOnClickListener {
				startActivity(Intent(activity, BluetoothDeviceListActivity::class.java))
			}
		}
	}

	private fun setCookTime(){
		val cook = SettingPref(activity!!).cookTime
		val minute = "${cook}m"
		_binding.tvStCookTime.text = minute
		if (cook <= MIN_COOK){
			_binding.btnStCookMin.visibility = View.INVISIBLE
		}else{
			_binding.btnStCookMin.visibility = View.VISIBLE
		}
	}
}
