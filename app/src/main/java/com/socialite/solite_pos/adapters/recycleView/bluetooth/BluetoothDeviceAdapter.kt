package com.socialite.solite_pos.adapters.recycleView.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.databinding.RvStringListBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class BluetoothDeviceAdapter(
	private val savedAddress: String?,
	private val callback: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.ListViewHolder>() {

	private var devices: ArrayList<BluetoothDevice> = ArrayList()

	fun setDevices(devices: ArrayList<BluetoothDevice>) {
		val deviceDiffUtils = RecycleViewDiffUtils(this.devices, devices)
		val diffUtilResult = DiffUtil.calculateDiff(deviceDiffUtils)
		this.devices = devices
		diffUtilResult.dispatchUpdatesTo(this)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvStringListBinding.inflate(
			inflater,
			parent,
			false
		)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setTitleView(devices[position])
		holder.setRootView(devices[position])
	}

	override fun getItemCount(): Int {
		return devices.size
	}

	inner class ListViewHolder(private val binding: RvStringListBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun setTitleView(device: BluetoothDevice) {
			val text = if (ActivityCompat.checkSelfPermission(
					binding.root.context,
					Manifest.permission.BLUETOOTH_CONNECT
				) != PackageManager.PERMISSION_GRANTED
			) {
				"${device.name} \n ${device.address}".trimIndent()
			} else ""

			binding.tvRvSt.text = text
		}

		fun setRootView(device: BluetoothDevice) {
			if (device.address == savedAddress) {
				val color = ContextCompat.getColor(itemView.context, R.color.colorAccent)
				val colorList = ColorStateList.valueOf(color)
				binding.root.setCardBackgroundColor(colorList)
			} else {
				binding.root.setOnClickListener { callback.invoke(device) }
			}
		}
	}
}
