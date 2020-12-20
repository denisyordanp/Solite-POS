package com.sosialite.solite_pos.data.source.remote.response.helper

class ApiResponse<T> private constructor(

		var status: StatusResponse,

		val body: T?,

		val message: String?) {

	companion object {

		fun <T> success(body: T?): ApiResponse<T?> {
			return ApiResponse(StatusResponse.SUCCESS, body, null)
		}


		fun <T> empty(msg: String?, body: T?): ApiResponse<T?> {
			return ApiResponse(StatusResponse.EMPTY, body, msg)
		}


		fun <T> error(msg: String?, body: T?): ApiResponse<T?> {
			return ApiResponse(StatusResponse.ERROR, body, msg)
		}
	}
}
