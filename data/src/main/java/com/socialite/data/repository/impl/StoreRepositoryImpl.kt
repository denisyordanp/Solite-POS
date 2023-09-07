package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.StoreDao
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Store
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val dao: StoreDao,
    private val settingRepository: SettingRepository,
    private val db: AppDatabase
) : StoreRepository {
    override suspend fun getStore(id: String): Store? {
        return dao.getNewStore(id)
    }

    override fun getStores() = dao.getNewStores()
    override suspend fun getNeedUploadStores() = dao.getNeedUploadStores()
    override suspend fun insertStore(store: Store) {
        dao.insertStore(store)
    }
    override suspend fun updateStore(store: Store) = dao.updateNewStore(store)
    override suspend fun migrateToUUID() {
        val stores = dao.getStores().firstOrNull()
        val activeStore = settingRepository.getSelectedStore().first()
        if (!stores.isNullOrEmpty()) {
            db.withTransaction {
                for (store in stores) {
                    val uuid = store.new_id.ifEmpty {
                        val updatedStore = store.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        dao.updateStore(updatedStore)

                        if (activeStore != 0L && activeStore == updatedStore.id) {
                            settingRepository.selectNewStore(updatedStore.new_id)
                        }

                        updatedStore.new_id
                    }

                    val newStore = Store(
                        id = uuid,
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

    override suspend fun deleteAllNewStores() {
        dao.deleteAllNewStore()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Store>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun getItems() = dao.getNewStores().first()
    override suspend fun insertItems(items: List<Store>) {
        dao.insertStores(items)
    }
    override suspend fun updateItems(items: List<Store>) {
        dao.updateStores(items)
    }
}
