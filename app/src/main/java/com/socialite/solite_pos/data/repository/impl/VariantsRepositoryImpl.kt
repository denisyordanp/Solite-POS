package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.new_master.Variant
import com.socialite.solite_pos.database.AppDatabase
import com.socialite.solite_pos.database.dao.VariantsDao
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.data.repository.VariantsRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class VariantsRepositoryImpl @Inject constructor(
    private val dao: VariantsDao,
    private val db: AppDatabase
) : VariantsRepository {

    companion object {
        @Volatile
        private var INSTANCE: VariantsRepositoryImpl? = null

        fun getInstance(
            dao: VariantsDao,
            db: AppDatabase
        ): VariantsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(VariantsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = VariantsRepositoryImpl(dao = dao, db = db)
                    }
                }
            }
            return INSTANCE!!
        }
    }

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
