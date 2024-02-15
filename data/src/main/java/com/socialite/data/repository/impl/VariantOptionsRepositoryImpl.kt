package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.database.AppDatabase
import com.socialite.core.database.dao.VariantOptionsDao
import com.socialite.core.database.dao.VariantsDao
import com.socialite.data.repository.SyncRepository
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.new_master.VariantOption
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class VariantOptionsRepositoryImpl @Inject constructor(
    private val dao: VariantOptionsDao,
    private val variantsDao: VariantsDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : VariantOptionsRepository {

    override fun getVariantOptions() = dao.getNewVariantOptionsFlow().flowOn(dispatcher)

    override suspend fun getNeedUploadVariantOptions() = dao.getNeedUploadVariantOptions()

    override suspend fun insertVariantOption(data: VariantOption) {
        dao.insertNewVariantOption(data)
    }

    override suspend fun updateVariantOption(data: VariantOption) {
        dao.updateNewVariantOption(
            data.copy(
                isUploaded = false
            )
        )
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
