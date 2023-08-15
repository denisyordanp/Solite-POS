package com.socialite.solite_pos.utils.printer

import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.schema.room.new_master.VariantOption
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.thousand
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrintBill {

    fun doPrint(
        outputStream: OutputStream,
        order: OrderWithProduct,
        type: PrinterUtils.PrintType
    ) {
        try {
            when (type) {
                PrinterUtils.PrintType.BILL -> outputStream.printBill(order)
                PrinterUtils.PrintType.QUEUE -> outputStream.printQueue(order)
            }

            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun OutputStream.printQueue(order: OrderWithProduct) {
        setHeaderQueue(order)
        setItemsQueue(order)
        printNewLine(PrinterUtils.TextSpaceLine.SMALL)
    }

    private fun OutputStream.printBill(order: OrderWithProduct) {
        setHeaderBill(order)
        setItemsBill(order)
        setFooter()
    }

    private fun OutputStream.setHeaderBill(order: OrderWithProduct) {
//		printLogo()
        printCustom(
            order.orderData.store.name,
            PrinterUtils.TextType.BOLD_MEDIUM,
            PrinterUtils.TextAlign.CENTER
        )
        printNewLine()
        printCustom(
            order.orderData.store.address,
            PrinterUtils.TextType.NORMAL,
            PrinterUtils.TextAlign.CENTER
        )
        printNewLine()
        printCustom(PrinterUtils.LINES, PrinterUtils.TextType.NORMAL, PrinterUtils.TextAlign.LEFT)
        printNewLine()
        printCustom(
            "Tgl : ${getDateTime()}",
            PrinterUtils.TextType.NORMAL,
            PrinterUtils.TextAlign.LEFT
        )
        printNewLine()
        printCustom(
            "No  : ${order.orderData.order.getQueueNumber()}",
            PrinterUtils.TextType.NORMAL,
            PrinterUtils.TextAlign.LEFT
        )
        printNewLine()
        setBasicHeader(order)
    }

    private fun OutputStream.setHeaderQueue(order: OrderWithProduct) {
        printNewLine()
        printCustom(
            order.orderData.order.getQueueNumber(),
            PrinterUtils.TextType.BOLD_LARGE,
            PrinterUtils.TextAlign.CENTER
        )
        printNewLine()
        setBasicHeader(order)
    }

    private fun OutputStream.setBasicHeader(order: OrderWithProduct) {
        printCustom("Nama: ", PrinterUtils.TextType.NORMAL, PrinterUtils.TextAlign.LEFT)
        printCustom(
            order.orderData.customer.name,
            PrinterUtils.TextType.NORMAL_BOLD,
            PrinterUtils.TextAlign.LEFT
        )
        printNewLine()
        val takeAway = if (order.orderData.order.isTakeAway) {
            "Take Away"
        } else {
            "Dine In"
        }
        printCustom(takeAway, PrinterUtils.TextType.NORMAL_BOLD, PrinterUtils.TextAlign.RIGHT)
        printNewLine()
        printCustom(PrinterUtils.LINES, PrinterUtils.TextType.NORMAL, PrinterUtils.TextAlign.LEFT)
        printNewLine()
    }

    private fun OutputStream.setItemsBill(order: OrderWithProduct) {
        if (order.products.isNotEmpty()) {
            for (item in order.products) {
                if (item.product != null) {
                    printCustom(
                        item.product.name,
                        PrinterUtils.TextType.NORMAL_BOLD,
                        PrinterUtils.TextAlign.LEFT
                    )
                    printNewLine()

                    printVariants(item.variants)

                    printCustom(
                        PrinterUtils.withSpace(
                            "  ${item.amount} x ${toRupiah(item.product.price)}",
                            "= ${toRupiah(item.amount * item.product.price)}",
                            32
                        ), PrinterUtils.TextType.NORMAL, PrinterUtils.TextAlign.LEFT
                    )
                    printNewLine()
                }
            }
            printCustom(
                PrinterUtils.LINES,
                PrinterUtils.TextType.NORMAL,
                PrinterUtils.TextAlign.CENTER
            )
            printTotal(order)
        }
    }

    private fun OutputStream.printVariants(variants: List<VariantOption>) {
        if (variants.isNotEmpty()) {
            val lastIndex = variants.size - 1
            variants.forEachIndexed { index, variant ->
                if (index == 0) printCustom(
                    " ",
                    PrinterUtils.TextType.NORMAL,
                    PrinterUtils.TextAlign.LEFT
                )
                printCustom(
                    " ${variant.name}",
                    PrinterUtils.TextType.NORMAL,
                    PrinterUtils.TextAlign.LEFT
                )
                if (index != lastIndex) printCustom(
                    "-",
                    PrinterUtils.TextType.NORMAL,
                    PrinterUtils.TextAlign.LEFT
                )
            }
        }
        printNewLine()
    }

    private fun OutputStream.printTotal(order: OrderWithProduct) {
        if (order.orderData.payment != null) {
            printNewLine()
            printCustom(
                PrinterUtils.withSpace("Total   : Rp.", thousand(order.grandTotal), 21),
                PrinterUtils.TextType.NORMAL_BOLD,
                PrinterUtils.TextAlign.RIGHT
            )
            printNewLine()

            if (order.orderData.payment.isCash) {
                printCustom(
                    PrinterUtils.withSpace(
                        "Bayar   : Rp.",
                        thousand(order.orderData.orderPayment?.pay),
                        21
                    ), PrinterUtils.TextType.NORMAL_BOLD, PrinterUtils.TextAlign.RIGHT
                )
                printNewLine()
                printCustom(
                    PrinterUtils.withSpace(
                        "Kembali : Rp.",
                        thousand(order.orderData.orderPayment?.inReturn(order.grandTotal)),
                        21
                    ), PrinterUtils.TextType.NORMAL_BOLD, PrinterUtils.TextAlign.RIGHT
                )
                printNewLine()
            } else {
                printCustom(
                    PrinterUtils.withSpace("Bayar   :", order.orderData.payment.name, 21),
                    PrinterUtils.TextType.NORMAL_BOLD,
                    PrinterUtils.TextAlign.RIGHT
                )
                printNewLine()
            }
        }
    }

    private fun OutputStream.setItemsQueue(order: OrderWithProduct) {
        if (order.products.isNotEmpty()) {
            for ((i, item) in order.products.withIndex()) {
                if (item.product != null) {
                    printCustom(
                        "${i + 1}. ${item.product.name} x${item.amount}",
                        PrinterUtils.TextType.NORMAL,
                        PrinterUtils.TextAlign.LEFT
                    )
                    printNewLine()

                    for (variant in item.variants) {
                        printCustom(
                            " ${variant.name}",
                            PrinterUtils.TextType.NORMAL_BOLD,
                            PrinterUtils.TextAlign.LEFT
                        )
                    }
                    printNewLine(PrinterUtils.TextSpaceLine.SMALL)
                }
            }
            printNewLine()
        }
    }

    private fun OutputStream.setFooter() {
        printNewLine(PrinterUtils.TextSpaceLine.MEDIUM)
        printCustom(
            "Terima kasih atas kunjungannya",
            PrinterUtils.TextType.NORMAL_BOLD,
            PrinterUtils.TextAlign.CENTER
        )
        printNewLine(PrinterUtils.TextSpaceLine.MEDIUM)
    }

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

    private fun getDateTime(): String {
        val l = Locale.getDefault()
        val dateF = SimpleDateFormat("EE, d/M/y H:m", l)
        return dateF.format(Date())
    }
}
