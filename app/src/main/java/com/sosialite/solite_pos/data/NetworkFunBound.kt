@file:Suppress("LeakingThis")

package com.sosialite.solite_pos.data

import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse

abstract class NetworkFunBound<RequestType, ResultType> {

	protected abstract fun dbOperation() : ResultType
	protected abstract fun createCall(savedData: ResultType, inCallback: (ApiResponse<RequestType>) -> Unit)
	protected abstract fun successUpload(before: ResultType)
	protected abstract fun failedUpload()
	protected abstract fun dbFinish(savedData: ResultType)

	init {
		val save = dbOperation()
		createCall(save){
			when(it.status){
				StatusResponse.SUCCESS -> {
					if (it.body != null)
						successUpload(save)
					else
						failedUpload()
				}
				StatusResponse.ERROR -> failedUpload()
				else -> failedUpload()
			}
		}
		dbFinish(save)
	}
}
