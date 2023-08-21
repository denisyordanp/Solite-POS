package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.OutcomesDao
import com.socialite.data.database.dao.StoreDao
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Outcome
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.ReportParameter
import com.socialite.data.schema.helper.UpdateSynchronizations
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import java.util.UUID
import javax.inject.Inject

class OutcomesRepositoryImpl @Inject constructor(
    private val dao: OutcomesDao,
    private val storesDao: StoreDao,
    private val settingRepository: SettingRepository,
    private val db: AppDatabase
) : OutcomesRepository {

    companion object {
        @Volatile
        private var INSTANCE: OutcomesRepositoryImpl? = null

        fun getInstance(
            dao: OutcomesDao,
            storesDao: StoreDao,
            settingRepository: SettingRepository,
            db: AppDatabase
        ): OutcomesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OutcomesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OutcomesRepositoryImpl(
                                dao = dao,
                                storesDao = storesDao,
                                settingRepository = settingRepository,
                                db = db
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }
    @FlowPreview
    override fun getOutcomes(parameters: ReportParameter) = if (parameters.isTodayOnly()) {
        settingRepository.getNewSelectedStore().flatMapConcat {
            dao.getOutcome(parameters.start, parameters.end, it)
        }
    } else {
        dao.getOutcome(parameters.start, parameters.end, parameters.storeId)
    }

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
                            isUploaded = outcome.isUploaded
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