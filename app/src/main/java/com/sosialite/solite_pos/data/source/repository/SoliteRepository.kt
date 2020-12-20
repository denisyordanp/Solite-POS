package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sosialite.solite_pos.data.source.local.room.LocalDataSource
import com.sosialite.solite_pos.data.source.remote.RemoteDataSource
import com.sosialite.solite_pos.utils.database.AppExecutors

class SoliteRepository private constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource, private val appExecutors: AppExecutors) : SoliteDataSource {

	companion object {
		@Volatile
		private var INSTANCE: SoliteRepository? = null
//		private val TAG = BaseKey.DATA_LOGS + BerkarirRepository::class.java.simpleName

		fun getInstance(remoteData: RemoteDataSource, localDataSource: LocalDataSource, appExecutors: AppExecutors): SoliteRepository {
			if (INSTANCE == null) {
				synchronized(SoliteRepository::class.java) {
					if (INSTANCE == null) {
						INSTANCE = SoliteRepository(remoteData, localDataSource, appExecutors)
					}
				}
			}
			return INSTANCE!!
		}
	}
}
