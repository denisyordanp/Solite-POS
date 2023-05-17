package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.VariantsDao
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

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

    override fun getVariants() = dao.getVariants()

    override suspend fun insertVariant(data: Variant) {
        dao.insertVariant(data)
    }

    override suspend fun updateVariant(data: Variant) {
        dao.updateVariant(data)
    }

    override suspend fun migrateToUUID() {
        val variants = dao.getVariants().first()
        db.withTransaction {
            for (variant in variants) {
                dao.updateVariant(variant.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
