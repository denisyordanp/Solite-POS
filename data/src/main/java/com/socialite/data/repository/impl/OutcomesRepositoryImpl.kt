package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.database.AppDatabase
import com.socialite.core.database.dao.OutcomesDao
import com.socialite.core.database.dao.StoreDao
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.new_master.Outcome
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class OutcomesRepositoryImpl @Inject constructor(
    private val dao: OutcomesDao,
    private val storesDao: StoreDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : OutcomesRepository {

    override fun getOutcomes(from: String, until: String, store: String, userId: Long) =
        dao.getOutcome(from, until, store, userId).flowOn(dispatcher)

    override fun getOutcomesAllStore(
        from: String,
        until: String,
        userId: Long
    ) = dao.getOutcome(from, until, userId).flowOn(dispatcher)

    override fun getOutcomesAllUser(
        from: String,
        until: String,
        store: String
    ) = dao.getOutcome(from, until, store).flowOn(dispatcher)

    override fun getOutcomesAllStoreAndUser(from: String, until: String) =
        dao.getOutcome(from, until).flowOn(dispatcher)

    override suspend fun insertOutcome(data: Outcome) {
        dao.insertNewOutcome(data)
    }

    override suspend fun migrateToUUID() {
        val outcomes = dao.getOutcomes()
        db.withTransaction {
            for (outcome in outcomes) {
                val uuid = outcome.new_id.ifEmpty {
                    val updatedOutcome = outcome.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOutcome(updatedOutcome)
                    updatedOutcome.new_id
                }

                val store = storesDao.getStore(outcome.store)
                if (store != null) {
                    val newOutcome = Outcome(
                        id = uuid,
                        name = outcome.name,
                        desc = outcome.desc,
                        price = outcome.price,
                        amount = outcome.amount,
                        date = outcome.date,
                        store = store.new_id,
                        isUploaded = outcome.isUploaded,
                        user = 0L
                    )
                    dao.insertNewOutcome(newOutcome)
                }
            }
        }
    }

    override suspend fun getNeedUploadOutcomes(): List<Outcome> = dao.getNeedUploadOutcomes()

    override suspend fun getItems(): List<Outcome> {
        return dao.getNewOutcomes()
    }

    override suspend fun updateItems(items: List<Outcome>) {
        dao.updateOutcomes(items)
    }

    override suspend fun insertItems(items: List<Outcome>) {
        dao.insertOutcomes(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Outcome>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun deleteAllOldOutcomes() {
        dao.deleteAllOldOutcomes()
    }

    override suspend fun deleteAllNewOutcomes() {
        dao.deleteAllNewOutcomes()
    }
}
