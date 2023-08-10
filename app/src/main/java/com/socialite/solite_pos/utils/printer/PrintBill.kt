package com.socialite.solite_pos.utils.printer

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.thousand
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrintBill {

    fun doPrint(
        socket: BluetoothSocket,
        order: OrderWithProduct,
        type: PrintType,
        onFinished: (Boolean) -> Unit
    ) {
        val outputStream = socket.outputStream

        //print command
        try {
            when (type) {
                PrintType.BILL -> outputStream.printBill(order)
                PrintType.QUEUE -> outputStream.printQueue(order)
            }

            outputStream!!.flush()
            onFinished.invoke(true)
        } catch (e: IOException) {
            Log.e("Print Bill", "${e.message}")
            e.printStackTrace()
            onFinished.invoke(false)
        } finally {
            outputStream.close()
        }
    }

    private fun OutputStream.printQueue(order: OrderWithProduct) {
        setHeaderQueue(order)
        setItemsQueue(order)
        printNewLine(4)
    }

    private fun OutputStream.printBill(order: OrderWithProduct) {
        setHeaderBill(order)
        setItemsBill(order)
        setFooter()
        //resetPrint();
    }

    private fun OutputStream.setHeaderBill(order: OrderWithProduct) {
//		printLogo()
        printCustom(order.orderData.store.name, 1, 1)
        printNewLine(1)
        printCustom(order.orderData.store.address, 0, 1)
//		printNewLine(1)
//		printCustom("Cimahi Tengah", 0, 1)
        printNewLine(1)
        printCustom(PrinterUtils.LINES, 0, 0)
        printNewLine(1)
        printCustom("Tgl : ${getDateTime()}", 0, 0)
        printNewLine(1)
        printCustom("No  : ${order.orderData.order.getQueueNumber()}", 0, 0)
        printNewLine(1)
        setBasicHeader(order)
    }

    private fun OutputStream.setHeaderQueue(order: OrderWithProduct) {
        printNewLine(2)
        printCustom(order.orderData.order.getQueueNumber(), 3, 1)
        printNewLine(1)
        setBasicHeader(order)
    }

    private fun OutputStream.setBasicHeader(order: OrderWithProduct) {
        printCustom("Nama: ", 0, 0)
        printCustom(order.orderData.customer.name, 1, 0)
        printNewLine(1)
        val takeAway = if (order.orderData.order.isTakeAway) {
            "Take Away"
        } else {
            "Dine In"
        }
        printCustom(takeAway, 1, 2)
        printNewLine(1)
        printCustom(PrinterUtils.LINES, 0, 0)
        printNewLine(1)
    }

    private fun OutputStream.setItemsBill(order: OrderWithProduct) {
        if (order.products.isNotEmpty()) {
            for ((i, item) in order.products.withIndex()) {
                if (item.product != null) {
                    printCustom("${i + 1}. ${item.product.name}", 0, 0)

                    for (variant in item.variants) {
                        printCustom(" ${variant.name}", 1, 0)
                    }
                    printNewLine(1)

                    for (variant in item.variants) {
                        printCustom(" ${variant.name}", 1, 0)
                    }
                    printNewLine(1)

                    printCustom(
                        withSpace(
                            "  ${item.amount} x ${toRupiah(item.product.price)}",
                            "= ${toRupiah(item.amount * item.product.price)}",
                            32
                        ), 0, 0
                    )
                    printNewLine(1)
                }
            }
            printCustom(PrinterUtils.LINES21, 0, 2)
            printNewLine(1)
            printTotal(order)
        }
    }

    private fun OutputStream.printTotal(order: OrderWithProduct) {
        if (order.orderData.payment != null) {
            printCustom(withSpace("Total   : Rp.", thousand(order.grandTotal), 21), 1, 2)
            printNewLine(1)

            if (order.orderData.payment.isCash) {
                printCustom(
                    withSpace(
                        "Bayar   : Rp.",
                        thousand(order.orderData.orderPayment?.pay),
                        21
                    ), 1, 2
                )
                printNewLine(1)
                printCustom(
                    withSpace(
                        "Kembali : Rp.",
                        thousand(order.orderData.orderPayment?.inReturn(order.grandTotal)),
                        21
                    ), 1, 2
                )
                printNewLine(1)
            } else {
                printCustom(withSpace("Bayar   :", order.orderData.payment.name, 21), 1, 2)
                printNewLine(1)
            }
            printCustom(PrinterUtils.LINES, 0, 0)
        }
    }

    private fun OutputStream.setItemsQueue(order: OrderWithProduct) {
        if (order.products.isNotEmpty()) {
            for ((i, item) in order.products.withIndex()) {
                if (item.product != null) {
                    printCustom("${i + 1}. ${item.product.name} x${item.amount}", 0, 0)

                    for (variant in item.variants) {
                        printCustom(" ${variant.name}", 1, 0)
                    }
                    printNewLine(1)

                    for (variant in item.variants) {
                        printCustom(" ${variant.name}", 1, 0)
                    }
                    printNewLine(1)
                    printNewLine(1)
                }
            }
            printNewLine(1)
        }
    }

//	set footer

    private fun OutputStream.setFooter() {
        printCustom("Terima kasih atas kunjungannya", 1, 1)
//		printNewLine(2)
//		printCustom("Kritik Saran mohon sampaikan ke", 1, 1)
//		printNewLine(1)
//		printCustom("FB / IG : jajanansosialita", 1, 1)
//		printNewLine(1)
//		printCustom("WA : 0821-1711-6825", 1, 1)
        printNewLine(5)
    }

    //print custom

    private fun OutputStream.printCustom(msg: String, size: Int, align: Int) {
        //Print config "mode"
        val cc = byteArrayOf(0x1B, 0x21, 0x03) // 0- normal size text
        val bb = byteArrayOf(0x1B, 0x21, 0x08) // 1- only bold text
        val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text
        val bb3 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
        try {
            when (size) {
                0 -> this.write(cc)
                1 -> this.write(bb)
                2 -> this.write(bb2)
                3 -> this.write(bb3)
            }
            when (align) {
                0 ->                     //left align
                    this.write(PrinterCommands.ESC_ALIGN_LEFT)

                1 ->                     //center align
                    this.write(PrinterCommands.ESC_ALIGN_CENTER)

                2 ->                     //right align
                    this.write(PrinterCommands.ESC_ALIGN_RIGHT)
            }
            this.write(msg.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //print logo

//	private fun printLogo() {
//		try {
//			val bmp = BitmapFactory.decodeResource(
//				activity.resources,
//				R.drawable.logo_jansos
//			)
//			if (bmp != null) {
//				val command: ByteArray? = PrinterUtils.decodeBitmap(bmp)
//				outputStream?.write(PrinterCommands.ESC_ALIGN_CENTER)
//				printText(command)
//			} else {
//				Log.e("Print Photo error", "the file isn't exists")
//			}
//		} catch (e: Exception) {
//			e.printStackTrace()
//			Log.e("PrintTools", "the file isn't exists")
//		}
//	}

    //print new line

    private fun OutputStream.printNewLine(count: Int) {
        repeat(count) {
            try {
                this.write(PrinterCommands.FEED_LINE)
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

//    private fun OutputStream.printText(msg: ByteArray?) {
//        try {
//            // Print normal text
//            this.write(msg)
//            printNewLine(1)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    private fun getDateTime(): String {
        val l = Locale.getDefault()
        val dateF = SimpleDateFormat("EE, d/M/y H:m", l)
        return dateF.format(Date())
    }

    private fun withSpace(str1: String, str2: String?, length: Int): String {
        return if (str2 != null) {
            val loop = length - (str1.length + str2.length)
            var space = ""
            repeat(loop) {
                space = "$space "
            }
            "$str1$space$str2"
        } else {
            ""
        }
    }

    enum class PrintType {
        BILL, QUEUE
    }
}
