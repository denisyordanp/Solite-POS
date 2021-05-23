@file:Suppress("LeakingThis")

package com.socialite.solite_pos.data

import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.data.source.remote.response.helper.StatusResponse

abstract class NetworkInsertUpdateResource<RequestType, ResultType, CallBackType>
protected constructor(
		val callback: (ApiResponse<CallBackType>) -> Unit
) {

	protected abstract fun dbOperation() : ResultType
	protected abstract fun createCall(savedData: ResultType, inCallback: (ApiResponse<RequestType>) -> Unit)
	protected abstract fun successUpload(before: ResultType)
	protected abstract fun dbFinish(savedData: ResultType)

	init {
		val save = dbOperation()
		createCall(save){
			when(it.status){
				StatusResponse.SUCCESS -> {
					if (it.body != null)
						successUpload(save)
					else
						failed()
				}
				StatusResponse.ERROR -> failed()
				else -> failed()
			}
		}
		dbFinish(save)
	}

	private fun failed(){
		callback.invoke(ApiResponse.error(null))
	}
}
