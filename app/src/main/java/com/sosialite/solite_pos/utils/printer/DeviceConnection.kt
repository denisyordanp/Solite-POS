package com.sosialite.solite_pos.utils.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.widget.Toast
import com.sosialite.solite_pos.utils.config.SettingPref
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import java.io.IOException
import java.util.*

class DeviceConnection(private val activity: SocialiteActivity) {
	private var mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
	private var setting: SettingPref = SettingPref(activity)

	companion object{
		var mbtSocket: BluetoothSocket? = null
	}

	fun getDevice(callback: (Boolean) -> Unit){
		if (setting.printerDevice.isNullOrEmpty()){
			val intent = Intent(
					activity.applicationContext,
					BluetoothDeviceListActivity::class.java
			)
			activity.startActivityForResult(intent, 0)
		}else{
			val device = mBluetoothAdapter.getRemoteDevice(setting.printerDevice)
			onDevice(device, callback)
		}
	}

	private fun onDevice(device: BluetoothDevice, callback: (Boolean) -> Unit){
		if (mBluetoothAdapter.isDiscovering) {
			mBluetoothAdapter.cancelDiscovery()
		}

		Toast.makeText(activity, "Printing", Toast.LENGTH_SHORT).show()

		Thread {
			try {
				val uuid: UUID = device.uuids[0].uuid
				mbtSocket = device.createRfcommSocketToServiceRecord(uuid)
				mbtSocket?.connect()
				SettingPref(activity.applicationContext).printerDevice = device.address
				callback.invoke(true)
			} catch (ex: IOException) {
				activity.runOnUiThread(socketErrorRunnable)
				try {
					mbtSocket?.close()
				} catch (e: IOException) {
					e.printStackTrace()
				}
				mbtSocket = null
				callback.invoke(false)
			}
		}.start()
	}

	private val socketErrorRunnable = Runnable {
		setting.printerDevice = ""
		Toast.makeText(activity,
				"Cannot establish connection", Toast.LENGTH_SHORT).show()
		mBluetoothAdapter.startDiscovery()
	}
}
