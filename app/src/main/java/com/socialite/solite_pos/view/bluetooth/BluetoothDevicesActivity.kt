package com.socialite.solite_pos.view.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.printer.PrinterConnection
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BluetoothDevicesActivity : SoliteActivity() {

    private val mBluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private val viewModel: BluetoothDevicesViewModel by viewModels()

    companion object {
        private const val REQUEST_ENABLE_BT = 4444
        private const val EXTRA_ORDER_ID = "extra_order_id"
        private const val EXTRA_PRINT_TYPE = "extra_print_type"

        fun createIntent(
            context: Context,
            orderId: String,
            printType: PrintBill.PrintType
        ): Intent {
            val intent = Intent(context, BluetoothDevicesActivity::class.java).apply {
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_PRINT_TYPE, printType)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orderId = intent.getStringExtra(EXTRA_ORDER_ID)!!
        val printType = intent.getSerializableExtra(EXTRA_PRINT_TYPE) as PrintBill.PrintType

        setContent {
            SolitePOSTheme {
                val devices = viewModel.bluetoothDevices.collectAsState().value
                val order = viewModel.getOrder(orderId).collectAsState(initial = null).value

                if (devices.isNotEmpty() && order != null) {
                    BluetoothContent(
                        devices = devices,
                        onChooseDevice = {
                            onChooseDevice(it, order, printType)
                        }
                    )
                } else {
                    checkBluetooth()

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun BluetoothContent(
        devices: List<BluetoothDevice>,
        onChooseDevice: (BluetoothDevice) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(devices) {
                Text(
                    modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp)
						.clickable {
							onChooseDevice(it)
						},
                    text = "${it.name} \n ${it.address}"
                )
            }
        }
    }

    private fun checkBluetooth() {
        if (isBluetoothAvailable()) {
            if (mBluetoothAdapter!!.isEnabled) {
                getBoundDevice(mBluetoothAdapter!!)
            } else {
                forceToEnableBluetooth()
            }
        } else {
            showToast("Perangkat tidak mendukung bluetooth")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BT) {
            getBoundDevice(mBluetoothAdapter!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getBoundDevice(bluetoothAdapter: BluetoothAdapter) {
        whenPermissionGranted {
            val boundedDevice = bluetoothAdapter.bondedDevices
            if (!boundedDevice.isNullOrEmpty()) {
                viewModel.setDevices(boundedDevice.toList())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun forceToEnableBluetooth() {
        whenPermissionGranted {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun whenPermissionGranted(granted: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            granted()
        }
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (reqCode == REQUEST_ENABLE_BT) {
            if (mBluetoothAdapter!!.isEnabled) {
                getBoundDevice(mBluetoothAdapter!!)
            } else {
                showToast("Anda harus mengaktifkan bluetooth untuk menggunakan fitur print")
            }
        }
    }

    private fun isBluetoothAvailable(): Boolean {
        return mBluetoothAdapter != null
    }

    @SuppressLint("MissingPermission")
    private fun onChooseDevice(
        device: BluetoothDevice,
        order: OrderWithProduct,
        printType: PrintBill.PrintType
    ) {
        viewModel.setNewPrinterDevice(device.address)

        whenPermissionGranted {
            PrinterConnection.getSocketFromDevice(device) { socket ->
                if (socket != null) {
                    PrintBill.doPrint(
                        socket = socket,
                        order = order,
                        type = printType,
                        onFinished = {
                            socket.close()
                            finish()
                        })
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopDiscovering()
    }

    @SuppressLint("MissingPermission")
    private fun stopDiscovering() {
        whenPermissionGranted {
            mBluetoothAdapter?.cancelDiscovery()
        }
    }
}
