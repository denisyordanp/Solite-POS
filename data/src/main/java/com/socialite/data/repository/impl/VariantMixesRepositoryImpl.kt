package com.socialite.data.repository.impl

import com.socialite.data.database.dao.VariantMixesDao
import com.socialite.data.repository.VariantMixesRepository
import com.socialite.data.schema.room.bridge.VariantMix

class VariantMixesRepositoryImpl(
    private val dao: VariantMixesDao
) : VariantMixesRepository {

    companion object {
        @Volatile
        private var INSTANCE: VariantMixesRepositoryImpl? = null

        fun getInstance(
            dao: VariantMixesDao
        ): VariantMixesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(VariantMixesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = VariantMixesRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getVariantMixProductById(idVariant: Long, idProduct: Long) =
        dao.getVariantMixProductById(idVariant, idProduct)

    override fun getVariantMixProduct(idVariant: Long) = dao.getVariantMixProduct(idVariant)

    override suspend fun insertVariantMix(data: VariantMix) {
        dao.insertVariantMix(data)
    }

    override suspend fun removeVariantMix(data: VariantMix) {
        dao.removeVariantMix(data.idVariant)
    }
}
