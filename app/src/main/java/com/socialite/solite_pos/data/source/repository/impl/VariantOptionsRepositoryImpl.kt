package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.local.room.VariantsDao
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import kotlinx.coroutines.flow.map
import java.util.UUID

class VariantOptionsRepositoryImpl(
    private val dao: VariantOptionsDao,
    private val variantsDao: VariantsDao,
    private val db: AppDatabase
) : VariantOptionsRepository {

    companion object {
        @Volatile
        private var INSTANCE: VariantOptionsRepositoryImpl? = null

        fun getInstance(
            dao: VariantOptionsDao,
            variantsDao: VariantsDao,
            db: AppDatabase
        ): VariantOptionsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(VariantOptionsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = VariantOptionsRepositoryImpl(
                            dao = dao,
                            db = db,
                            variantsDao = variantsDao
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getVariantOptions(query: SupportSQLiteQuery) = dao.getVariantOptions(query)
    override fun getVariantsWithOptions() = dao.getVariantWithOptions().map { list ->
        list.groupBy { it.variant }
            .map {
                VariantWithOptions(
                    variant = it.key,
                    options = it.value.map { option ->
                        option.option
                    }
                )
            }
    }

    override suspend fun getNeedUploadVariantOptions() = dao.getNeedUploadVariantOptions()

    override suspend fun insertVariantOption(data: VariantOption) {
        dao.insertNewVariantOption(data)
    }
    override suspend fun insertVariantOptions(list: List<VariantOption>) {
        dao.insertVariantOptions(list)
    }
    override suspend fun updateVariantOption(data: VariantOption) {
        dao.updateNewVariantOption(data.copy(
            isUploaded = false
        ))
    }

    override suspend fun migrateToUUID() {
        val variantOptions = dao.getVariantOptions()
        db.withTransaction {
            for (variantOption in variantOptions) {
                val uuid = variantOption.new_id.ifEmpty {
                    val updatedVariantOption = variantOption.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateVariantOption(updatedVariantOption)
                    updatedVariantOption.new_id
                }

                val variant = variantsDao.getVariantById(variantOption.idVariant)
                if (variant != null) {
                    val newVariantOption = VariantOption(
                        id = uuid,
                        variant = variant.new_id,
                        name = variantOption.name,
                        desc = variantOption.desc,
                        isActive = variantOption.isActive,
                        isUploaded = variantOption.isUploaded
                    )
                    dao.insertNewVariantOption(newVariantOption)
                }
            }
        }
    }

    override suspend fun deleteAllOldVariantOptions() {
        dao.deleteAllOldVariantOptions()
    }

    override suspend fun deleteAllNewVariantOptions() {
        dao.deleteAllNewVariantOptions()
    }

    override suspend fun getItems(): List<VariantOption> {
        return dao.getNewVariantOptions()
    }

    override suspend fun updateItems(items: List<VariantOption>) {
        dao.updateVariantOptions(items)
    }

    override suspend fun insertItems(items: List<VariantOption>) {
        dao.insertVariantOptions(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<VariantOption>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
