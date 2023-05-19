package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo as NewPromo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.repository.PromosRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
        val promos = dao.getPromos(Promo.filter(Promo.Status.ALL)).firstOrNull()
        if (!promos.isNullOrEmpty()) {
            db.withTransaction {
                for (promo in promos) {
                    dao.updatePromo(promo.copy(
                        new_id = UUID.randomUUID().toString()
                    ))
                }
                val updatedPromos = dao.getPromos(Promo.filter(Promo.Status.ALL)).first()
                for (promo in updatedPromos) {
                    val newPromo = NewPromo(
                        id = promo.new_id,
                        name = promo.name,
                        desc = promo.desc,
                        isCash = promo.isCash,
                        value = promo.value,
                        isActive = promo.isActive,
                        isUploaded = promo.isUploaded
                    )
                    dao.insertNewPromo(newPromo)
                }
            }
        }
    }
}
