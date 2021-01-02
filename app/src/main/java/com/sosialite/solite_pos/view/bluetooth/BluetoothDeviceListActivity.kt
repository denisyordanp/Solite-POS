package com.sosialite.solite_pos.view.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.databinding.ActivityBluetoothDeviceListBinding
import com.sosialite.solite_pos.view.bluetooth.adapter.DeviceAdapter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class BluetoothDeviceListActivity : AppCompatActivity() {

	private var mBluetoothAdapter: BluetoothAdapter? = null

	private var btDevices: ArrayList<BluetoothDevice> = ArrayList()

	private val mBTReceiver: BroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			val action = intent.action
			if (BluetoothDevice.ACTION_FOUND == action) {
				val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
				if (device != null){
					try {
						if (btDevices.indexOf(device) < 0) {
							btDevices.add(device)
							adapter.addItem(device)
						}
					} catch (ex: Exception) {
						ex.fillInStackTrace()
					}
				}
			}
		}
	}

	private lateinit var binding: ActivityBluetoothDeviceListBinding
	private lateinit var adapter: DeviceAdapter

	companion object{

		private var TAG = BluetoothDeviceListActivity::class.java.simpleName

		private var mbtSocket: BluetoothSocket? = null

		const val REQUEST_ENABLE_BT = 0

		fun getSocket(): BluetoothSocket? {
			return mbtSocket
		}
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		binding = ActivityBluetoothDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

		adapter = DeviceAdapter {onChooseDevice(it)}

		binding.rvBtDvList.layoutManager = LinearLayoutManager(this)
		binding.rvBtDvList.adapter = adapter


		try {
			if (initDevicesList() != 0) {
				finish()
			}
		} catch (ex: Exception) {
			finish()
		}

		proceedDiscovery()
    }

	override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
		super.onActivityResult(reqCode, resultCode, intent)
		if (reqCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			val btDeviceList: Set<BluetoothDevice>? = mBluetoothAdapter?.bondedDevices
			if (btDeviceList != null){
				try {
					if (btDeviceList.isNotEmpty()) {
						for (device in btDeviceList) {
							if (!btDevices.contains(device)) {
								btDevices.add(device)

							}
						}
						adapter.setItems(btDevices)
					}
				} catch (ex: Exception) {
					Log.e(TAG, ex.message!!)
				}
			}
		}
		mBluetoothAdapter?.startDiscovery()
	}

	override fun onStop() {
		super.onStop()
		try {
			unregisterReceiver(mBTReceiver)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	private fun proceedDiscovery() {
		val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
		registerReceiver(mBTReceiver, filter)

		mBluetoothAdapter?.startDiscovery()
	}

	private fun initDevicesList(): Int {
		flushData()

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter!!.isDiscovering) {
				mBluetoothAdapter?.cancelDiscovery()
			}
		}else{
			Toast.makeText(
					applicationContext,
					"Bluetooth not supported!!", Toast.LENGTH_LONG
			).show()
			return -1
		}

		val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
		try {
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
		} catch (ex: Exception) {
			return -2
		}
		Toast.makeText(applicationContext,
				"Getting all available Bluetooth Devices", Toast.LENGTH_SHORT).show()
		return 0
	}

	private fun flushData() {
		try {
			if (mbtSocket != null) {
				mbtSocket?.close()
				mbtSocket = null
			}
			mBluetoothAdapter?.cancelDiscovery()

			btDevices.clear()

		} catch (ex: Exception) {
			Log.e(TAG, ex.message!!)
		}
	}

	private fun onChooseDevice(device: BluetoothDevice){
		if (mBluetoothAdapter == null) {
			return
		}else{
			if (mBluetoothAdapter!!.isDiscovering) {
				mBluetoothAdapter!!.cancelDiscovery()
			}

			Toast.makeText(
					applicationContext, "Connecting to ${device.name}, ${device.address}",
					Toast.LENGTH_SHORT).show()

			val connectThread = Thread {
				try {
//					val uuid: Boolean = device.fetchUuidsWithSdp()
					val uuid: UUID = device.uuids[0].uuid
					mbtSocket = device.createRfcommSocketToServiceRecord(uuid)
					mbtSocket?.connect()
				} catch (ex: IOException) {
					runOnUiThread(socketErrorRunnable)
					try {
						mbtSocket?.close()
					} catch (e: IOException) {
						e.printStackTrace()
					}
					mbtSocket = null
				} finally {
					runOnUiThread { finish() }
				}
			}

			connectThread.start()
		}
	}

	private val socketErrorRunnable = Runnable {
		Toast.makeText(applicationContext,
				"Cannot establish connection", Toast.LENGTH_SHORT).show()
		mBluetoothAdapter?.startDiscovery()
	}
}
