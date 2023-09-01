package com.socialite.solite_pos.view.screens.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.socialite.domain.schema.OrderWithProduct
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.printer.PrinterConnection
import com.socialite.solite_pos.utils.printer.PrinterUtils
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BluetoothDevicesActivity : SoliteActivity() {

    //TODO: Try connection on different android versions

    private val mBluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private val viewModel: BluetoothDevicesViewModel by viewModels()

    companion object {
        private const val REQUEST_ENABLE_BT = 4444
        private const val EXTRA_ORDER_ID = "extra_order_id"
        private const val EXTRA_PRINT_TYPE = "extra_print_type"

        fun createIntent(
            context: Context,
            orderId: String,
            printType: PrinterUtils.PrintType
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
        val printType = intent.getSerializableExtra(EXTRA_PRINT_TYPE) as PrinterUtils.PrintType

        viewModel.getOrder(orderId)
        checkBluetooth()

        setContent {
            SolitePOSTheme {
                val snackBarHostState = remember { SnackbarHostState() }

                Scaffold(
                    topBar = {
                        Column {
                            BasicTopBar(
                                titleText = "Perangkat bluetooth",
                                onBackClicked = {
                                    onBackPressed()
                                }
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                text = "Perangkat printer harus sudah Paired terlebih dahulu",
                                fontStyle = FontStyle.Italic
                            )
                        }
                    },
                    content = { padding ->
                        val viewState = viewModel.viewState.collectAsState().value

                        LaunchedEffect(key1 = viewState.shouldSHowSnackBar) {
                            if (viewState.shouldSHowSnackBar) {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Anda harus mengizinkan koneksi bluetooth untuk menggunakan fitur print",
                                    actionLabel = "Buka pengaturan",
                                )
                                when (result) {
                                    SnackbarResult.Dismissed -> viewModel.resetSnackBar()
                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.resetSnackBar()
                                        openSettingsApp()
                                    }
                                }
                            }
                        }

                        if (viewState.isLoading) {
                            // TODO: Add connection timeout then set tryConnecting to false
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            if (viewState.devices.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .padding(padding)
                                        .fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Perangkat tidak ditemukan")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        PrimaryButtonView(
                                            buttonText = "Coba lagi",
                                            onClick = {
                                                viewModel.reCheckBluetooth()
                                                checkBluetooth()
                                            }
                                        )
                                    }
                                }
                            } else {
                                BluetoothContent(
                                    modifier = Modifier.padding(padding),
                                    devices = viewState.devices,
                                    onChooseDevice = {
                                        onChooseDevice(it, viewState.order!!, printType)
                                    }
                                )
                            }
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun BluetoothContent(
        modifier: Modifier = Modifier,
        devices: List<BluetoothDevice>,
        onChooseDevice: (BluetoothDevice) -> Unit
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(devices) {
                Divider()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onChooseDevice(it)
                        }
                        .padding(16.dp),
                    text = "${it.name} \n ${it.address}"
                )
            }
        }
    }

    private fun checkBluetooth() {
        if (isBluetoothAvailable()) {
            checkBluetoothPermission { isGranted ->
                if (isGranted) {
                    getBoundDevice(mBluetoothAdapter!!)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        requestPermissions(
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                            REQUEST_ENABLE_BT
                        )
                    } else {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Perangkat tidak mendukung bluetooth", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getBoundDevice(mBluetoothAdapter!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getBoundDevice(bluetoothAdapter: BluetoothAdapter) {
        checkBluetoothPermission {
            if (!bluetoothAdapter.isEnabled) bluetoothAdapter.enable()
            val boundedDevice = bluetoothAdapter.bondedDevices
            if (!boundedDevice.isNullOrEmpty()) {
                viewModel.setDevices(boundedDevice.toList())
            }
        }
    }

    private fun checkBluetoothPermission(granted: (Boolean) -> Unit) {
        when {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> granted(true)

            shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT) -> {
                viewModel.showSnackBar()
            }

            else -> granted(false)
        }
    }

    private fun openSettingsApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (reqCode == REQUEST_ENABLE_BT) {
            getBoundDevice(mBluetoothAdapter!!)
        }
    }

    private fun isBluetoothAvailable(): Boolean {
        return mBluetoothAdapter != null
    }

    @SuppressLint("MissingPermission")
    private fun onChooseDevice(
        device: BluetoothDevice,
        order: OrderWithProduct,
        printType: PrinterUtils.PrintType
    ) {
        viewModel.setNewPrinterDevice(device.address)

        checkBluetoothPermission {
            PrinterConnection(lifecycleScope).getSocketFromDevice(
                device = device,
                print = {
                    PrintBill.doPrint(
                        outputStream = it,
                        order = order,
                        type = printType,
                    )
                },
                onError = {
                    when (it) {
                        PrinterConnection.PrintConnectionErrors.FAILED_TO_CONNECT -> {
                            Toast.makeText(
                                this,
                                "Tidak dapat koneksi ke perangkat, mohon periksa perangkat",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        PrinterConnection.PrintConnectionErrors.NEED_NEW_CONNECTION -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan, mohon coba lagi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
    }
}