package com.sosialite.solite_pos.utils.printer

import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import java.io.IOException
import java.io.OutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class PrintBill(private var activity: AppCompatActivity) {

	private var btsocket: BluetoothSocket? = null
	private var outputStream: OutputStream? = null
	private var items: ArrayList<DetailOrder>? = null
	private var pay: Int? = null

	fun doPrint(items: ArrayList<DetailOrder>?, pay: Int?){
		this.items = items
		this.pay = pay
		if (btsocket == null) {
			val intent = Intent(
				activity.applicationContext,
				BluetoothDeviceListActivity::class.java
			)
			activity.startActivityForResult(intent, BluetoothDeviceListActivity.REQUEST_CONNECT_BT)
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
		if (!items.isNullOrEmpty()){
			var grand = 0
			for ((i, item) in items!!.withIndex()){
				val total = item.amount * item.product.price
				grand += total
				printCustom("${i+1}.${item.product.name}", 0, 0)
				printNewLine()
				printCustom(withSpace("  ${item.amount} x Rp.${thousand(item.product.price)}", "= Rp.${thousand(total)}", 32), 0, 0)
				printNewLine()
			}
			printCustom(PrinterUtils.LINES21, 0, 2)
			printNewLine()
			printTotal(grand)
		}
	}

//	print total
	private fun printTotal(total: Int) {
		if (pay != null){
			printCustom(withSpace("Total   : Rp.", thousand(total), 21), 1, 2)
			printNewLine()
			printCustom(withSpace("Bayar   : Rp.", thousand(pay!!), 21), 1, 2)
			printNewLine()
			printCustom(withSpace("Kembali : Rp.", thousand(pay!!-total), 21), 1, 2)
			printNewLine()
			printCustom(PrinterUtils.LINES, 0, 0)
		}
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
		printCustom("No  : 654125/8", 0, 0)
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
		val dateF = SimpleDateFormat("d/M/y H:m", l)
		val dayF = SimpleDateFormat("EEEE", l)
		val date = dateF.format(Date())
		val day = dayF.format(Date())
		return "${getDay(day)},$date"
	}

	private fun getDay(day: String): String{
		return when(day){
			"Sunday" -> "Min"
			"Monday" -> "Sen"
			"Tuesday" -> "Sel"
			"Wednesday" -> "Rab"
			"Thursday" -> "Kam"
			"Friday" -> "Jum"
			"Saturday" -> "Sab"
			else -> ""
		}
	}

	private fun thousand(number: Int): String{
		return NumberFormat.getNumberInstance(Locale.getDefault()).format(number)
	}

	private fun withSpace(str1: String, str2: String, length: Int): String {
		val loop = length - (str1.length + str2.length)
		var space = ""
		repeat(loop){
			space = "$space "
		}
		return "$str1$space$str2"
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
			doPrint(items, pay)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}
