package com.socialite.solite_pos.view.screens.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.preference.SettingPreferences
import com.socialite.solite_pos.data.domain.GetOrderWithProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothDevicesViewModel @Inject constructor(
    private val getOrderWithProduct: GetOrderWithProduct,
    private val settingPreferences: SettingPreferences
) : ViewModel() {
    private val _viewState = MutableStateFlow(BluetoothViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun setDevices(devices: List<BluetoothDevice>) = viewModelScope.launch {
        _viewState.emit(currentState.copy(
            devices = devices
        ))
    }

    fun getOrder(orderId: String) = viewModelScope.launch {
        getOrderWithProduct(orderId)
            .map {
                currentState.copy(
                    order = it
                )
            }.collect(_viewState)
    }

    fun setNewPrinterDevice(address: String) {
        settingPreferences.printerDevice = address
    }

    fun showSnackBar() = viewModelScope.launch {
        _viewState.emit(currentState.copy(
            shouldSHowSnackBar = true
        ))
    }

    fun resetSnackBar() = viewModelScope.launch {
        _viewState.emit(currentState.copy(
            shouldSHowSnackBar = false,
            isTryConnecting = false
        ))
    }

    fun reCheckBluetooth() = viewModelScope.launch {
        _viewState.emit(currentState.copy(
            isTryConnecting = true
        ))
    }

    private val currentState get() = _viewState.value
}
