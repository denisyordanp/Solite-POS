package com.socialite.solite_pos.utils.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.socialite.solite_pos.utils.preference.SettingPref
import java.io.IOException
import java.util.*

class DeviceConnection(private val activity: FragmentActivity) {

	private var setting: SettingPref = SettingPref(activity)

	companion object {
		var mbtSocket: BluetoothSocket? = null

		fun setSocketFromDevice(device: BluetoothDevice) {
			val uuid: UUID = device.uuids[0].uuid
			try {
				mbtSocket = device.createRfcommSocketToServiceRecord(uuid)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}

	fun getDevice(callback: (BluetoothSocket?) -> Unit) {
		val address = setting.printerDevice
		if (!address.isNullOrEmpty()) {
			val device = getDeviceFromAddress(address)
			if (device != null) {
				setSocketFromDevice(device)
				if (mbtSocket != null) {
					connectSocket(mbtSocket!!, callback)
				} else {
					callback.invoke(null)
				}
			} else {
				callback.invoke(null)
			}
		} else {
			callback.invoke(null)
		}
	}

	private fun getDeviceFromAddress(address: String): BluetoothDevice? {
		val adapter = BluetoothAdapter.getDefaultAdapter()
		return if (adapter != null) {
			adapter.getRemoteDevice(address)
		} else {
			showToast("Bluetooth tidak aktif")
			null
		}
	}

	private fun connectSocket(socket: BluetoothSocket, callback: (BluetoothSocket?) -> Unit) {
		Thread {
			try {
				socket.connect()
				activity.runOnUiThread {
					showToast("Printing")
				}

				callback.invoke(mbtSocket)
			} catch (ex: IOException) {
				ex.printStackTrace()
				try {
					socket.close()
				} catch (e: IOException) {
					e.printStackTrace()
				}
				Log.e("DeviceConnection", "getDevice connect ${ex.message}");
				mbtSocket = null
				activity.runOnUiThread {
					showToast("Error print, periksa perangkat perinter lalu coba kembali")
				}
				callback.invoke(null)
			}
		}.start()
	}

	private fun showToast(message: String) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
	}
}
