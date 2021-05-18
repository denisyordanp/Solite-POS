package com.socialite.solite_pos.utils.printer

import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.thousand
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.preference.SettingPref
import com.socialite.solite_pos.view.bluetooth.BluetoothDeviceListActivity
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PrintBill(private var activity: FragmentActivity) {

	private var outputStream: OutputStream? = null
	private var callback: ((Boolean) -> Unit)? = null
	private var order: OrderWithProduct? = null
	private var type: Int = 0

	companion object {
		const val BILL = 11
		const val ORDER = 22
	}

	fun doPrint(order: OrderWithProduct, type: Int, callback: (Boolean) -> Unit) {
		this.order = order
		this.callback = callback
		this.type = type

		setData()
	}

	private fun setData() {
		DeviceConnection(activity).getDevice {
			if (it != null) {
				setPaper(it)
			} else {
				SettingPref(activity).printerDevice = ""
				toBluetoothDevice()
			}
		}
	}

	private fun toBluetoothDevice() {
		val intent = Intent(activity, BluetoothDeviceListActivity::class.java)
		intent.putExtra(BluetoothDeviceListActivity.EXTRA_ORDER, order)
		intent.putExtra(BluetoothDeviceListActivity.EXTRA_PRINT, type)
		activity.startActivity(intent)
	}

	private fun setPaper(socket: BluetoothSocket){

		//print command
		try {
			outputStream = socket.outputStream

//			print header
			setHeader()

//			print items
			setItems()

//			print footer
			setFooter()
			//resetPrint(); //reset printer

			outputStream!!.flush()
			callback?.invoke(true)
		} catch (e: IOException) {
			Log.e("Print Bill", "${e.message}")
			e.printStackTrace()
			callback?.invoke(false)
		}
	}

	private fun setItems() {
		if (!order?.products.isNullOrEmpty()){
			for ((i, item) in order?.products!!.withIndex()){
				if (item.product != null){
					printCustom("${i+1}.${item.product!!.name}", 0, 0)

					if (item.product!!.isMix){
						if (!item.mixProducts.isNullOrEmpty()) {
							printNewLine(1)
							for (mix in item.mixProducts) {
								printCustom("  - ${mix.product.name}", 0, 0)
								for (variant in mix.variants) {
									printCustom(" ${variant.name}", 1, 0)
								}
								printCustom(" x${mix.amount}", 0, 0)
								printNewLine(1)
							}
						}
					}else {
						for (variant in item.variants) {
							printCustom(" ${variant.name}", 1, 0)
						}
						printNewLine(1)
					}

					printCustom(
						withSpace(
							"  ${item.amount} x ${toRupiah(item.product!!.sellPrice)}",
							"= ${toRupiah(item.amount * item.product!!.sellPrice)}",
							32
						), 0, 0
					)
					printNewLine(1)
				}
			}
			printCustom(PrinterUtils.LINES21, 0, 2)
			printNewLine(1)
			if (type == BILL) {
				printTotal()
			}
		}
	}

//	print total
	private fun printTotal() {
		if (order?.order?.payment != null) {
			printCustom(withSpace("Total   : Rp.", thousand(order?.grandTotal), 21), 1, 2)
			printNewLine(1)

			if (order!!.order.payment != null) {
				if (order!!.order.payment!!.isCash) {
					printCustom(
						withSpace(
							"Bayar   : Rp.",
							thousand(order!!.order.orderPayment?.pay),
							21
						), 1, 2
					)
					printNewLine(1)
					printCustom(
						withSpace(
							"Kembali : Rp.",
							thousand(order!!.order.orderPayment?.inReturn(order!!.grandTotal)),
							21
						), 1, 2
					)
					printNewLine(1)
				} else {
					printCustom(withSpace("Bayar   :", order!!.order.payment!!.name, 21), 1, 2)
					printNewLine(1)
				}
			}
			printCustom(PrinterUtils.LINES, 0, 0)
		}
	}

//	set header

	private fun setHeader() {
		if (type == BILL) {
			printLogo()
			printCustom("Jl.Jend.Sudirman No.16G,Baros", 0, 1)
			printNewLine(1)
			printCustom("Cimahi Tengah", 0, 1)
			printNewLine(1)
			printCustom(PrinterUtils.LINES, 0, 0)
			printNewLine(1)
			printCustom("Tgl : ${getDateTime()}", 0, 0)
			printNewLine(1)
			printCustom("No  : ${order?.order?.order?.orderNo}", 0, 0)
			printNewLine(1)
		} else {
			printNewLine(2)
			printCustom("${order?.order?.order?.getQueueNumber()}", 3, 1)
			printNewLine(1)
		}

		printCustom("Nama: ", 0, 0)
		printCustom("${order?.order?.customer?.name}", 1, 0)
		printNewLine(1)
		val takeAway = if (order?.order?.order?.isTakeAway == true) {
			"Take Away"
		} else {
			"Dine In"
		}
		printCustom(takeAway, 1, 2)
		printNewLine(1)
		printCustom(PrinterUtils.LINES, 0, 0)
		printNewLine(1)
	}

//	set footer

	private fun setFooter() {
		if (type == BILL) {
			printCustom("Terima kasih atas kunjungannya", 1, 1)
			printNewLine(2)
			printCustom("Kritik Saran mohon sampaikan ke", 1, 1)
			printNewLine(1)
			printCustom("FB / IG : jajanansosialita", 1, 1)
			printNewLine(1)
			printCustom("WA : 0821-1711-6825", 1, 1)
		}
		printNewLine(5)
	}

	//print custom

	private fun printCustom(msg: String, size: Int, align: Int) {
		//Print config "mode"
		val cc = byteArrayOf(0x1B, 0x21, 0x03) // 0- normal size text
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

	private fun printNewLine(count: Int) {
		repeat(count) {
			try {
				outputStream?.write(PrinterCommands.FEED_LINE)
			} catch (e: IOException) {
				e.printStackTrace()
			}
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
			printNewLine(1)
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
			if (DeviceConnection.mbtSocket != null) {
				DeviceConnection.mbtSocket!!.close()
			}
			if (outputStream != null) {
				outputStream!!.close()
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}
