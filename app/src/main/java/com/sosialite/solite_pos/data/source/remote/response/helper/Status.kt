package com.sosialite.solite_pos.data.source.remote.response.helper

class Status(var status: Int, var isError: Boolean, private var message: String?) {
	fun getMessage(): String {
		return if (message == null) "" else message!!
	}

	fun setMessage(message: String?) {
		this.message = message
	}
}
