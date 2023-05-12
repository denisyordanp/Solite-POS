package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.VariantsDao
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant as NewVariant

class VariantsRepositoryImpl(
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
    override suspend fun insertVariant(data: NewVariant) {
        dao.insertNewVariant(data)
    }
    override suspend fun insertVariants(list: List<Variant>) {
        dao.insertVariants(list)
    }
    override suspend fun updateVariant(data: NewVariant) {
        dao.updateNewVariant(data)
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

                    val newVariant = NewVariant(
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
}
