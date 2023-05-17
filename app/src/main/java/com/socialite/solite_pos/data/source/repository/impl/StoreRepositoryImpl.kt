package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class StoreRepositoryImpl(
    private val dao: StoreDao,
    private val db: AppDatabase
) : StoreRepository {

    companion object {
        @Volatile
        private var INSTANCE: StoreRepositoryImpl? = null

        fun getInstance(
            dao: StoreDao,
            db: AppDatabase
        ): StoreRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(StoreRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoreRepositoryImpl(dao = dao, db = db)
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
    override suspend fun migrateToUUID() {
        val stores = dao.getStores().firstOrNull()
        if (!stores.isNullOrEmpty()) {
            db.withTransaction {
                for (store in stores) {
                    dao.updateStore(store.copy(
                        new_id = UUID.randomUUID().toString()
                    ))
                }
            }
        }
    }
}
