package com.socialite.solite_pos.utils.printer

import android.graphics.Bitmap
import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.util.*

fun OutputStream.printNewLine(space: PrinterUtils.TextSpaceLine = PrinterUtils.TextSpaceLine.NO_SPACE) {
	repeat(space.count) {
		try {
			this.write(PrinterCommands.FEED_LINE)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}

fun OutputStream.printCustom(msg: String, size: PrinterUtils.TextType, align: PrinterUtils.TextAlign) {
	try {
		when (size) {
			PrinterUtils.TextType.NORMAL -> this.write(PrinterUtils.cc)
			PrinterUtils.TextType.NORMAL_BOLD -> this.write(PrinterUtils.bb)
			PrinterUtils.TextType.BOLD_MEDIUM -> this.write(PrinterUtils.bb2)
			PrinterUtils.TextType.BOLD_LARGE -> this.write(PrinterUtils.bb3)
		}
		when (align) {
			PrinterUtils.TextAlign.LEFT -> this.write(PrinterCommands.ESC_ALIGN_LEFT)
			PrinterUtils.TextAlign.CENTER -> this.write(PrinterCommands.ESC_ALIGN_CENTER)
			PrinterUtils.TextAlign.RIGHT -> this.write(PrinterCommands.ESC_ALIGN_RIGHT)
		}
		this.write(msg.toByteArray())
	} catch (e: IOException) {
		e.printStackTrace()
	}
}

object PrinterUtils {

	enum class PrintType {
		BILL, QUEUE
	}

	enum class TextType {
		NORMAL, NORMAL_BOLD, BOLD_MEDIUM, BOLD_LARGE
	}

	enum class TextAlign {
		LEFT, CENTER, RIGHT
	}

	enum class TextSpaceLine(val count: Int) {
		NO_SPACE(1), SMALL(2), MEDIUM(3)
	}

	val cc = byteArrayOf(0x1B, 0x21, 0x03) // 0- normal size text
	val bb = byteArrayOf(0x1B, 0x21, 0x08) // 1- only bold text
	val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text
	val bb3 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text

	const val LINES = "--------------------------------"
	private const val hexStr = "0123456789ABCDEF"
	private val binaryArray = arrayOf("0000", "0001", "0010", "0011",
			"0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
			"1100", "1101", "1110", "1111")

	fun withSpace(str1: String, str2: String?, length: Int): String {
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

	fun decodeBitmap(bmp: Bitmap): ByteArray? {
		val bmpWidth = bmp.width
		val bmpHeight = bmp.height
		val list: MutableList<String> = ArrayList() //binaryString list
		var sb: StringBuffer
		val zeroCount = bmpWidth % 8
		var zeroStr = ""
		if (zeroCount > 0) {
			for (i in 0 until 8 - zeroCount) {
				zeroStr += "0"
			}
		}
		for (i in 0 until bmpHeight) {
			sb = StringBuffer()
			for (j in 0 until bmpWidth) {
				val color = bmp.getPixel(j, i)
				val r = color shr 16 and 0xff
				val g = color shr 8 and 0xff
				val b = color and 0xff

				// if color close to white，bit='0', else bit='1'
				if (r > 160 && g > 160 && b > 160) sb.append("0") else sb.append("1")
			}
			if (zeroCount > 0) {
				sb.append(zeroStr)
			}
			list.add(sb.toString())
		}
		val bmpHexList = binaryListToHexStringList(list)
		val commandHexString = "1D763000"
		var widthHexString = Integer
				.toHexString(if (bmpWidth % 8 == 0) bmpWidth / 8 else bmpWidth / 8 + 1)
		if (widthHexString.length > 2) {
			Log.e("decodeBitmap error", " width is too large")
			return null
		} else if (widthHexString.length == 1) {
			widthHexString = "0$widthHexString"
		}
		widthHexString += "00"
		var heightHexString = Integer.toHexString(bmpHeight)
		if (heightHexString.length > 2) {
			Log.e("decodeBitmap error", " height is too large")
			return null
		} else if (heightHexString.length == 1) {
			heightHexString = "0$heightHexString"
		}
		heightHexString += "00"
		val commandList: MutableList<String> = ArrayList()
		commandList.add(commandHexString + widthHexString + heightHexString)
		commandList.addAll(bmpHexList)
		return hexList2Byte(commandList)
	}

	private fun binaryListToHexStringList(list: List<String>): List<String> {
		val hexList: MutableList<String> = ArrayList()
		for (binaryStr in list) {
			val sb = StringBuffer()
			var i = 0
			while (i < binaryStr.length) {
				val str = binaryStr.substring(i, i + 8)
				val hexString = myBinaryStrToHexString(str)
				sb.append(hexString)
				i += 8
			}
			hexList.add(sb.toString())
		}
		return hexList
	}

	private fun myBinaryStrToHexString(binaryStr: String): String {
		var hex = ""
		val f4 = binaryStr.substring(0, 4)
		val b4 = binaryStr.substring(4, 8)
		for (i in binaryArray.indices) {
			if (f4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
		}
		for (i in binaryArray.indices) {
			if (b4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
		}
		return hex
	}

	private fun hexList2Byte(list: List<String>): ByteArray {
		val commandList: MutableList<ByteArray?> = ArrayList()
		for (hexStr in list) {
			commandList.add(hexStringToBytes(hexStr))
		}
		return sysCopy(commandList)
	}

	private fun hexStringToBytes(str: String?): ByteArray? {
		var hexString = str
		if (hexString == null || hexString == "") {
			return null
		}
		hexString = hexString.uppercase(Locale.getDefault())
		val length = hexString.length / 2
		val hexChars = hexString.toCharArray()
		val d = ByteArray(length)
		for (i in 0 until length) {
			val pos = i * 2
			d[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
		}
		return d
	}

	private fun sysCopy(srcArrays: List<ByteArray?>): ByteArray {
		var len = 0
		for (srcArray in srcArrays) {
			len += srcArray!!.size
		}
		val destArray = ByteArray(len)
		var destLen = 0
		for (srcArray in srcArrays) {
			if (srcArray != null){
				System.arraycopy(srcArray, 0, destArray, destLen, srcArray.size)
				destLen += srcArray.size
			}
		}
		return destArray
	}

	private fun charToByte(c: Char): Byte {
		return "0123456789ABCDEF".indexOf(c).toByte()
	}
}
