@file:Suppress("LeakingThis")

package com.sosialite.solite_pos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.sosialite.solite_pos.utils.database.AppExecutors
import com.sosialite.solite_pos.vo.Resource

abstract class NetworkBoundResource<ResultType, RequestType> protected constructor(private val mExecutors: AppExecutors) {

	protected abstract fun loadFromDB(): LiveData<ResultType>
	protected abstract fun shouldFetch(data: ResultType): Boolean
	protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
	protected abstract fun saveCallResult(data: RequestType?)

	private val result = MediatorLiveData<Resource<ResultType>>()

	init {
		result.value = Resource.loading(null)
		val dbSource = loadFromDB()
		result.addSource(dbSource) { data: ResultType ->
			result.removeSource(dbSource)
			if (shouldFetch(data)) {
				fetchFromNetwork(dbSource)
			} else {
				result.addSource(dbSource) { newData: ResultType ->
					result.setValue(Resource.success(newData))
				}
			}
		}
	}

	private fun onFetchFailed() {}

	private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
		val apiResponse = createCall()
		result.addSource(dbSource) { newData: ResultType -> result.setValue(Resource.loading(newData)) }
		result.addSource(apiResponse) { response: ApiResponse<RequestType> ->
			result.removeSource(apiResponse)
			result.removeSource(dbSource)
			when (response.status) {
				StatusResponse.SUCCESS -> mExecutors.diskIO().execute {
					saveCallResult(response.body)
					mExecutors.mainThread().execute {
						result.addSource(loadFromDB()) { newData: ResultType ->
							result.setValue(Resource.success(newData))
						}
					}
				}
				StatusResponse.EMPTY -> mExecutors.mainThread().execute {
					result.addSource(loadFromDB()) { newData: ResultType ->
						result.setValue(Resource.success(newData))
					}
				}
				StatusResponse.ERROR -> {
					onFetchFailed()
					result.addSource(dbSource) { newData: ResultType ->
						result.setValue(Resource.error(response.message, newData))
					}
				}
			}
		}
	}

	fun asLiveData(): LiveData<Resource<ResultType>> {
		return result
	}
}
