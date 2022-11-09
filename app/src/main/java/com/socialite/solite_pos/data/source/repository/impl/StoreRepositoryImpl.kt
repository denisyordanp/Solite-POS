package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.StoreRepository
import kotlinx.coroutines.flow.Flow

class StoreRepositoryImpl(
    private val dao: StoreDao
) : StoreRepository {

    companion object {
        @Volatile
        private var INSTANCE: StoreRepositoryImpl? = null

        fun getInstance(
            dao: StoreDao
        ): StoreRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(StoreRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoreRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getStores(): Flow<List<Store>> = dao.getStores()
    override suspend fun getStore(id: Long) = dao.getStore(id)
    override suspend fun insertStore(store: Store) = dao.insertStore(store)
    override suspend fun updateStore(store: Store) = dao.updateStore(store)
}
