package com.socialite.solite_pos.data.source.repository.impl

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository

class VariantOptionsRepositoryImpl(
    private val dao: VariantOptionsDao
) : VariantOptionsRepository {

    companion object {
        @Volatile
        private var INSTANCE: VariantOptionsRepositoryImpl? = null

        fun getInstance(
            dao: VariantOptionsDao
        ): VariantOptionsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(VariantOptionsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = VariantOptionsRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getVariantOptions(query: SupportSQLiteQuery) = dao.getVariantOptions(query)

    override suspend fun insertVariantOption(data: VariantOption) {
        dao.insertVariantOption(data)
    }

    override suspend fun updateVariantOption(data: VariantOption) {
        dao.updateVariantOption(data)
    }
}
