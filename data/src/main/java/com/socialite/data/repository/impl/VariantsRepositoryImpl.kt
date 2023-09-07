package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.VariantsDao
import com.socialite.data.repository.SyncRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Variant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class VariantsRepositoryImpl @Inject constructor(
    private val dao: VariantsDao,
    private val db: AppDatabase
) : VariantsRepository {

    override fun getVariants() = dao.getNewVariants()
    override suspend fun getNeedUploadVariants() = dao.getNeedUploadVariants()
    override suspend fun insertVariant(data: Variant) {
        dao.insertNewVariant(data)
    }
    override suspend fun updateVariant(data: Variant) {
        dao.updateNewVariant(data.copy(
            isUploaded = false
        ))
    }

    override suspend fun migrateToUUID() {
        val variants = dao.getVariants().firstOrNull()
        if (!variants.isNullOrEmpty()) {
            db.withTransaction {
                for (variant in variants) {
                    val uuid = variant.new_id.ifEmpty {
                        val updatedVariant = variant.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        updatedVariant.id = variant.id
                        dao.updateVariant(updatedVariant)
                        updatedVariant.new_id
                    }

                    val newVariant = Variant(
                        id = uuid,
                        name = variant.name,
                        type = variant.type,
                        isMust = variant.isMust,
                        isUploaded = variant.isUploaded
                    )
                    dao.insertNewVariant(newVariant)
                }
            }
        }
    }

    override suspend fun deleteAllOldVariants() {
        dao.deleteAllOldVariants()
    }

    override suspend fun deleteAllNewVariants() {
        dao.deleteAllNewVariants()
    }

    override suspend fun getItems(): List<Variant> {
        return dao.getNewVariants().first()
    }

    override suspend fun updateItems(items: List<Variant>) {
        dao.updateVariants(items)
    }

    override suspend fun insertItems(items: List<Variant>) {
        dao.insertVariants(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Variant>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
