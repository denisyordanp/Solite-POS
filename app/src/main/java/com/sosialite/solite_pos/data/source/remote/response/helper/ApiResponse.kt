package com.sosialite.solite_pos.data.source.remote.response.helper

class ApiResponse<T> private constructor(

		var status: StatusResponse,

		val body: T?,

		val message: String?)
{

	companion object {

		fun <T> success(body: T): ApiResponse<T> {
			return ApiResponse(StatusResponse.SUCCESS, body, null)
		}

		fun <T> finish(body: T): ApiResponse<T> {
			return ApiResponse(StatusResponse.FINISH, body, null)
		}

		fun <T> empty(msg: String?): ApiResponse<T> {
			return ApiResponse(StatusResponse.EMPTY, null, msg)
		}

		fun <T> error(msg: String?): ApiResponse<T> {
			return ApiResponse(StatusResponse.ERROR, null, msg)
		}
	}
}
