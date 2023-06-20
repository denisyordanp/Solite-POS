package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.OutcomesDao
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import java.util.UUID

class OutcomesRepositoryImpl(
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

    override fun getOutcomes(date: String) = dao.getOutcome(date)
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
    override suspend fun insertOutcomes(list: List<Outcome>) {
        dao.insertOutcomes(list)
    }

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
