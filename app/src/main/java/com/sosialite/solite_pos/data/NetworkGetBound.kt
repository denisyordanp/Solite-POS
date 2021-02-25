@file:Suppress("LeakingThis")

package com.sosialite.solite_pos.data

import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse

abstract class NetworkGetBound<T>
protected constructor(
		val callback: (ApiResponse<T>) -> Unit
) {

	protected abstract fun dbOperation() : T
	protected abstract fun shouldCall(data: T): Boolean
	protected abstract fun createCall(inCallback: (ApiResponse<T>) -> Unit)
	protected abstract fun successCall(result: T)
	protected abstract fun dbFinish(savedData: ApiResponse<T>)

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
