package com.socialite.solite_pos.view.screens.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.preference.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothDevicesViewModel @Inject constructor(
    private val getOrderWithProduct: GetOrderWithProduct,
    private val settingPreferences: SettingPreferences
) : ViewModel() {
    private val _devices = MutableStateFlow(listOf<BluetoothDevice>())
    val bluetoothDevices = _devices.asStateFlow()

    fun setDevices(devices: List<BluetoothDevice>) = viewModelScope.launch {
        _devices.emit(devices)
    }

    fun getOrder(orderId: String) = getOrderWithProduct(orderId)

    fun setNewPrinterDevice(address: String) {
        settingPreferences.printerDevice = address
    }
}
