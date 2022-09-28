package com.socialite.solite_pos.utils.di

import android.content.Context
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.local.room.LocalDataSource
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.impl.PaymentsRepositoryImpl
import com.socialite.solite_pos.utils.database.AppExecutors

object Injection {

	fun provideSoliteRepository(context: Context): SoliteRepository {
		val database = getInstance(context)
		val appExecutors = AppExecutors(context)
		val localDataSource = LocalDataSource.getInstance(database.soliteDao())
		return SoliteRepository.getInstance(appExecutors, localDataSource)
	}

	fun providePaymentsRepository(context: Context): PaymentsRepository {
		val database = getInstance(context)
		return PaymentsRepositoryImpl.getInstance(database.paymentsDao())
	}

}
