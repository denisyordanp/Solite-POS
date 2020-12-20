package com.sosialite.solite_pos.utils.di

import android.content.Context
import com.sosialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.sosialite.solite_pos.data.source.local.room.LocalDataSource
import com.sosialite.solite_pos.data.source.remote.RemoteDataSource.Companion.connect
import com.sosialite.solite_pos.data.source.repository.SoliteRepository
import com.sosialite.solite_pos.utils.database.AppExecutors

object Injection {

	fun provideSoliteRepository(context: Context): SoliteRepository {
		val database = getInstance(context)
		val remoteDataSource = connect()
		val localDataSource = LocalDataSource.getInstance(database.soliteDao())
		val appExecutors = AppExecutors(context)
		return SoliteRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
	}
}
