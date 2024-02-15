package com.socialite.solite_pos.view.screens.bluetooth

import android.bluetooth.BluetoothDevice
import com.socialite.schema.ui.helper.OrderWithProduct

data class BluetoothViewState(
    val devices: List<BluetoothDevice>,
    val shouldSHowSnackBar: Boolean,
    val isTryConnecting: Boolean,
    val order: OrderWithProduct?
) {

    val isLoading = (devices.isEmpty() || order == null) && isTryConnecting
    companion object {
        fun idle() = BluetoothViewState(
            devices = emptyList(),
            shouldSHowSnackBar = false,
            isTryConnecting = true,
            order = null
        )
    }
}
