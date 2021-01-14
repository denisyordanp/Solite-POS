package com.sosialite.solite_pos.utils.printer

import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.thousand
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PrintBill(private var activity: SocialiteActivity) {

	private var btsocket: BluetoothSocket? = null
	private var outputStream: OutputStream? = null
	private var order: Order? = null

	var payment: Payment? = null

	companion object{
		const val REQUEST_CONNECT_BT = 2
	}

	fun doPrint(order: Order?){
		this.order = order
		setData()
	}

	private fun setData(){
		if (btsocket == null) {
			val intent = Intent(
				activity.applicationContext,
				BluetoothDeviceListActivity::class.java
			)
			activity.startActivityForResult(intent, REQUEST_CONNECT_BT)
		} else {
			setPaper()
		}
	}

	private fun setPaper(){
		var opstream: OutputStream? = null
		try {
			opstream = btsocket?.outputStream
		} catch (e: IOException) {
			e.printStackTrace()
		}
		outputStream = opstream

		//print command
		try {
			try {
				Thread.sleep(1000)
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
			outputStream = btsocket?.outputStream

//			print header
			setHeader()

//			print items
			setItems()

//			print footer
			setFooter()
			//resetPrint(); //reset printer

			outputStream?.flush()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	private fun setItems() {
		TODO("selesaikan")
//		if (!order?.items.isNullOrEmpty()){
//			for ((i, item) in order?.items!!.withIndex()){
//				if (item.product != null){
//					printCustom("${i+1}.${item.product!!.name}", 0, 0)
//					printNewLine()
//					printCustom(withSpace("  ${item.amount} x ${toRupiah(item.product!!.price)}", "= ${toRupiah(item.amount * item.product!!.price)}", 32), 0, 0)
//					printNewLine()
//				}
//			}
//			printCustom(PrinterUtils.LINES21, 0, 2)
//			printNewLine()
//			printTotal()
//		}
	}

//	print total
	private fun printTotal() {
//		if (order?.pay != 0){
//			TODO("selesaikan")
//			printCustom(withSpace("Total   : Rp.", thousand(order?.totalPay), 21), 1, 2)
//			printNewLine()
//
//			if (payment != null && payment!!.name == "Tunai"){
//				printCustom(withSpace("Bayar   : Rp.", thousand(order?.pay), 21), 1, 2)
//				printNewLine()
//				TODO("selesaikan")
//				printCustom(withSpace("Kembali : Rp.", thousand(order?.payReturn), 21), 1, 2)
//				printNewLine()
//			}else{
//				printCustom(withSpace("Bayar   : Rp.", payment?.name, 21), 1, 2)
//				printNewLine()
//			}
//			printCustom(PrinterUtils.LINES, 0, 0)
//		}
	}

//	set header
	private fun setHeader() {
		printLogo()
		printCustom("Jl.Jend.Sudirman No.16G,Baros", 0, 1)
		printNewLine()
		printCustom("Cimahi Tengah", 0, 1)
		printNewLine()
		printCustom(PrinterUtils.LINES, 0, 0)
		printNewLine()
		printCustom("Tgl : ${getDateTime()}", 0, 0)
		printNewLine()
		printCustom("No  : ${order?.orderNo}", 0, 0)
		printNewLine()
	TODO("selesaikan")
//		printCustom("Nama: ${order?.customer?.name}", 0, 0)
		printNewLine()
		printCustom(PrinterUtils.LINES, 0, 0)
		printNewLine()
	}

//	set footer
	private fun setFooter() {
		printCustom("Terima kasih atas kunjungannya", 1, 1)
		printNewLine()
		printNewLine()
		printCustom("Kritik Saran mohon sampaikan ke", 1, 1)
		printNewLine()
		printCustom("FB / IG : jajanansosialita", 1, 1)
		printNewLine()
		printCustom("WA : 0821-1711-6825", 1, 1)
		printNewLine()
		printNewLine()
		printNewLine()
		printNewLine()
		printNewLine()
	}

	//print custom
	private fun printCustom(msg: String, size: Int, align: Int) {
		//Print config "mode"
		val cc = byteArrayOf(0x1B, 0x21, 0x03) // 0- normal size text
		//byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
		val bb = byteArrayOf(0x1B, 0x21, 0x08) // 1- only bold text
		val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text
		val bb3 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
		try {
			when (size) {
				0 -> outputStream?.write(cc)
				1 -> outputStream?.write(bb)
				2 -> outputStream?.write(bb2)
				3 -> outputStream?.write(bb3)
			}
			when (align) {
				0 ->                     //left align
					outputStream?.write(PrinterCommands.ESC_ALIGN_LEFT)
				1 ->                     //center align
					outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
				2 ->                     //right align
					outputStream?.write(PrinterCommands.ESC_ALIGN_RIGHT)
			}
			outputStream?.write(msg.toByteArray())
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	//print logo
	private fun printLogo() {
		try {
			val bmp = BitmapFactory.decodeResource(
				activity.resources,
				R.drawable.logo_jansos
			)
			if (bmp != null) {
				val command: ByteArray? = PrinterUtils.decodeBitmap(bmp)
				outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
				printText(command)
			} else {
				Log.e("Print Photo error", "the file isn't exists")
			}
		} catch (e: Exception) {
			e.printStackTrace()
			Log.e("PrintTools", "the file isn't exists")
		}
	}

	//print new line
	private fun printNewLine() {
		try {
			outputStream?.write(PrinterCommands.FEED_LINE)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

//	fun resetPrint() {
//		try {
//			outputStream?.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT)
//			outputStream?.write(PrinterCommands.FS_FONT_ALIGN)
//			outputStream?.write(PrinterCommands.ESC_ALIGN_LEFT)
//			outputStream?.write(PrinterCommands.ESC_CANCEL_BOLD)
//		} catch (e: IOException) {
//			e.printStackTrace()
//		}
//	}

//	print text
//	private fun printText(msg: String?) {
//		try {
//			// Print normal text
//			outputStream?.write(msg?.toByteArray())
//		} catch (e: IOException) {
//			e.printStackTrace()
//		}
//	}

	//print byte[]
	private fun printText(msg: ByteArray?) {
		try {
			// Print normal text
			outputStream?.write(msg)
			printNewLine()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	private fun getDateTime(): String {
		val l = Locale.getDefault()
		val dateF = SimpleDateFormat("EE, d/M/y H:m", l)
		return dateF.format(Date())
	}

	private fun withSpace(str1: String, str2: String?, length: Int): String {
		return if (str2 != null){
			val loop = length - (str1.length + str2.length)
			var space = ""
			repeat(loop){
				space = "$space "
			}
			"$str1$space$str2"
		}else{
			""
		}
	}

	fun onDestroy(){
		try {
			if (btsocket != null) {
				outputStream?.close()
				btsocket?.close()
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	fun onSetSocket(){
		try {
			btsocket = BluetoothDeviceListActivity.getSocket()
			doPrint(order)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}
