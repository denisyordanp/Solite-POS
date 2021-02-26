@file:Suppress("LeakingThis")

package com.sosialite.solite_pos.data

import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse

abstract class NetworkGetBound<RequestType, ResultType>
protected constructor(
		val callback: (ApiResponse<RequestType>) -> Unit
) {

	protected abstract fun dbOperation() : RequestType
	protected abstract fun shouldCall(data: RequestType): Boolean
	protected abstract fun createCall(inCallback: (ApiResponse<ResultType>) -> Unit)
	protected abstract fun successCall(result: ResultType)
	protected abstract fun dbFinish(savedData: ApiResponse<RequestType>)

	init {
		val get = dbOperation()
		if (shouldCall(get)){
			createCall {
				when(it.status){
					StatusResponse.SUCCESS -> {
						if (it.body != null){
							successCall(it.body)
							dbFinish(ApiResponse.success(dbOperation()))
						} else
							failed()
					}
					StatusResponse.EMPTY -> dbFinish(ApiResponse.empty(null))
					StatusResponse.ERROR -> failed()
					else -> failed()
				}
			}
		}else{
			dbFinish(ApiResponse.success(get))
		}
	}

	private fun failed(){
		callback.invoke(ApiResponse.error(null))
	}
}
