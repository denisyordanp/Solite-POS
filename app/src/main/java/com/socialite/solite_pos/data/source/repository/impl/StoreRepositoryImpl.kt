package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store as NewStore

class StoreRepositoryImpl(
    private val dao: StoreDao,
    private val settingRepository: SettingRepository,
    private val db: AppDatabase
) : StoreRepository {

    companion object {
        @Volatile
        private var INSTANCE: StoreRepositoryImpl? = null

        fun getInstance(
            dao: StoreDao,
            settingRepository: SettingRepository,
            db: AppDatabase
        ): StoreRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(StoreRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoreRepositoryImpl(
                            dao = dao,
                            db = db,
                            settingRepository = settingRepository
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getStores() = dao.getNewStores()
    override suspend fun getStore(id: String) = dao.getNewStore(id)
    override suspend fun insertStore(store: NewStore) {
        dao.insertStore(store)
        if (settingRepository.getNewSelectedStore().first().isEmpty()) {
            settingRepository.selectNewStore(store.id)
        }
    }
    override suspend fun updateStore(store: NewStore) = dao.updateNewStore(store)
    override suspend fun migrateToUUID() {
        val stores = dao.getStores().firstOrNull()
        val activeStore = settingRepository.getSelectedStore().first()
        if (!stores.isNullOrEmpty()) {
            db.withTransaction {
                for (store in stores) {
                    val updatedStore = store.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateStore(updatedStore)
                    if (activeStore != 0L && activeStore == updatedStore.id) {
                        settingRepository.selectNewStore(updatedStore.new_id)
                    }

                    val newStore = NewStore(
                        id = updatedStore.new_id,
                        name = store.name,
                        address = store.address,
                        isUploaded = false
                    )
                    dao.insertNewStore(newStore)
                }
            }
        }
    }

    override suspend fun deleteAllOldStores() {
        dao.deleteAllOldStore()
    }
}
