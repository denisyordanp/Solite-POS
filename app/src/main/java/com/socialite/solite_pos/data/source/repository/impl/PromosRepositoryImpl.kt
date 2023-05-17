package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.repository.PromosRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class PromosRepositoryImpl(
    private val dao: PromosDao,
    private val db: AppDatabase
) : PromosRepository {

    companion object {
        @Volatile
        private var INSTANCE: PromosRepositoryImpl? = null

        fun getInstance(
            dao: PromosDao,
            db: AppDatabase
        ): PromosRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PromosRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PromosRepositoryImpl(dao = dao, db = db)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun insertPromo(data: Promo) = dao.insertPromo(data)

    override suspend fun updatePromo(data: Promo) = dao.updatePromo(data)

    override fun getPromos(query: SupportSQLiteQuery) = dao.getPromos(query)
    override suspend fun migrateToUUID() {
        val promos = dao.getPromos(Promo.filter(Promo.Status.ALL)).first()
        db.withTransaction {
            for (promo in promos) {
                dao.updatePromo(promo.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
