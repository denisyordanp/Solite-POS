package com.socialite.solite_pos.utils.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.annotation.RequiresPermission
import androidx.fragment.app.FragmentActivity
import java.io.IOException
import java.io.OutputStream

class PrinterConnection(private val activity: FragmentActivity) {

    enum class PrintConnectionErrors {
        FAILED_TO_CONNECT, NEED_NEW_CONNECTION
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getSocketFromAddress(
        defaultAddress: String,
        print: (OutputStream) -> Unit,
        onError: ((PrintConnectionErrors) -> Unit)? = null
    ) {
        val device = getDeviceFromAddress(defaultAddress)
        if (device != null) {
            val socket = getSocket(device)
            if (socket != null) {
                tryToConnectTheSocket(socket, print, onError)
            } else {
                onError?.invoke(PrintConnectionErrors.NEED_NEW_CONNECTION)
            }
        } else {
            onError?.invoke(PrintConnectionErrors.NEED_NEW_CONNECTION)
        }
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getSocketFromDevice(
        device: BluetoothDevice,
        print: (OutputStream) -> Unit,
        onError: ((PrintConnectionErrors) -> Unit)? = null
    ) {
        val socket = getSocket(device)
        if (socket != null) {
            tryToConnectTheSocket(socket, print, onError)
        } else onError?.invoke(PrintConnectionErrors.NEED_NEW_CONNECTION)
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
    private fun tryToConnectTheSocket(
        socket: BluetoothSocket,
        print: (OutputStream) -> Unit,
        onError: ((PrintConnectionErrors) -> Unit)? = null
    ) {
        Thread {
            try {
                socket.connect()
                print(socket.outputStream)
                socket.outputStream.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
                activity.runOnUiThread {
                    onError?.invoke(PrintConnectionErrors.FAILED_TO_CONNECT)
                }
            } finally {
                try {
                    socket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}
