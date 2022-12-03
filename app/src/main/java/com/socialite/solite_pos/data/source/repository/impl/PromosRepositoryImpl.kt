package com.socialite.solite_pos.data.source.repository.impl

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.repository.PromosRepository

class PromosRepositoryImpl(
    private val dao: PromosDao
) : PromosRepository {

    companion object {
        @Volatile
        private var INSTANCE: PromosRepositoryImpl? = null

        fun getInstance(
            dao: PromosDao
        ): PromosRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PromosRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PromosRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun insertPromo(data: Promo) = dao.insertPromo(data)

    override suspend fun updatePromo(data: Promo) = dao.updatePromo(data)

    override fun getPromos(query: SupportSQLiteQuery) = dao.getPromos(query)
}
