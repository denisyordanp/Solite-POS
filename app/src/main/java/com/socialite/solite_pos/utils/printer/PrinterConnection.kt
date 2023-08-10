package com.socialite.solite_pos.utils.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.IOException

object PrinterConnection {

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getSocketFromAddress(
        defaultAddress: String,
        onSocketConnection: (BluetoothSocket?) -> Unit
    ) {
        if (defaultAddress.isNotEmpty()) {
            val device = getDeviceFromAddress(defaultAddress)
            if (device != null) {
                val socket = getSocket(device)
                if (socket != null) {
                    connectSocket(socket, onSocketConnection)
                } else {
                    onSocketConnection.invoke(null)
                }
            } else {
                onSocketConnection.invoke(null)
            }
        } else {
            onSocketConnection.invoke(null)
        }
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getSocketFromDevice(
        device: BluetoothDevice,
        onSocketConnection: (BluetoothSocket?) -> Unit
    ) {
        val socket = getSocket(device)
        return if (socket != null) {
            connectSocket(socket, onSocketConnection)
        } else onSocketConnection(null)
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun getSocket(device: BluetoothDevice): BluetoothSocket? {
        val uuid = device.uuids[0].uuid
        return try {
            device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getDeviceFromAddress(address: String): BluetoothDevice? {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter?.getRemoteDevice(address)
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun connectSocket(socket: BluetoothSocket, onResult: (BluetoothSocket?) -> Unit) {
        Thread {
            try {
                socket.connect()

                onResult.invoke(socket)
            } catch (ex: IOException) {
                ex.printStackTrace()
                try {
                    socket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Log.e("DeviceConnection", "getDevice connect ${ex.message}")
                onResult.invoke(null)
            }
        }.start()
    }
}
