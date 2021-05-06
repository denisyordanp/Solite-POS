package com.socialite.solite_pos.view.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.databinding.ActivityBluetoothDeviceListBinding
import com.socialite.solite_pos.utils.preference.SettingPref
import com.socialite.solite_pos.utils.printer.DeviceConnection
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.bluetooth.adapter.DeviceAdapter

class BluetoothDeviceListActivity : SocialiteActivity() {

	private var btDevices: ArrayList<BluetoothDevice> = ArrayList()

	private var mBluetoothAdapter: BluetoothAdapter? = null
	private lateinit var binding: ActivityBluetoothDeviceListBinding
	private lateinit var adapter: DeviceAdapter
	private lateinit var setting: SettingPref

	companion object {
		const val REQUEST_ENABLE_BT = 0
	}

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		binding = ActivityBluetoothDeviceListBinding.inflate(layoutInflater)
		setContentView(binding.root)

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
		setting = SettingPref(this)
		adapter = DeviceAdapter(setting.printerDevice) { onChooseDevice(it) }

		binding.rvBtDvList.layoutManager = LinearLayoutManager(this)
		binding.rvBtDvList.adapter = adapter

		checkBluetooth()
	}

	private fun checkBluetooth() {
		if (isBluetoothAvailable()) {
			proceedDiscoveryDevice(mBluetoothAdapter!!)
		} else {
			forceToEnableBluetooth()
		}
	}

	private fun proceedDiscoveryDevice(bluetoothAdapter: BluetoothAdapter) {
		showToast("Mencari perangkat")
		val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
		registerReceiver(mBTReceiver, filter)

		bluetoothAdapter.startDiscovery()
	}

	private fun forceToEnableBluetooth() {
		val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
	}

	override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
		super.onActivityResult(reqCode, resultCode, intent)
		if (reqCode == REQUEST_ENABLE_BT) {
			if (isBluetoothAvailable()) {
				checkBoundedDevice(mBluetoothAdapter!!)
			} else {
				showToast("Anda harus mengaktifkan bluetooth untuk menggunakan fitur print")
			}
		} else {
			showToast("Anda harus mengaktifkan bluetooth untuk menggunakan fitur print")
		}
	}

	private fun isBluetoothAvailable(): Boolean {
		return mBluetoothAdapter != null
	}

	private fun checkBoundedDevice(bluetoothAdapter: BluetoothAdapter) {
		val btDeviceList: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
		if (!btDeviceList.isNullOrEmpty()) {
			for (device in btDeviceList) {
				if (!btDevices.contains(device)) {
					btDevices.add(device)
				}
			}
			adapter.setItems(btDevices)
		}
		proceedDiscoveryDevice(bluetoothAdapter)
	}

	private val mBTReceiver: BroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			val action = intent.action
			if (BluetoothDevice.ACTION_FOUND == action) {

				val device = intent.getParcelableExtra<BluetoothDevice>(
					BluetoothDevice.EXTRA_DEVICE
				)

				addDeviceToAdapter(device)
			}
		}
	}

	private fun addDeviceToAdapter(device: BluetoothDevice?) {
		if (device != null) {
			if (!btDevices.contains(device)) {
				btDevices.add(device)
				adapter.addItem(device)
			}
		}
	}

	private fun onChooseDevice(device: BluetoothDevice) {
		setting.printerDevice = device.address
		DeviceConnection.setSocketFromDevice(device)
		setResult(PrintBill.REQUEST_CONNECT_BT)
		finish()
	}

	private fun showToast(message: String) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}

	override fun onStop() {
		super.onStop()
		unregisterReceiver(mBTReceiver)
		stopDiscovering()
	}

	private fun stopDiscovering() {
		mBluetoothAdapter?.cancelDiscovery()
	}
}
