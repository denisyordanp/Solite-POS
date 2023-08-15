package com.socialite.solite_pos.data.repository.impl

import com.socialite.solite_pos.data.schema.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.room.VariantMixesDao
import com.socialite.solite_pos.data.repository.VariantMixesRepository

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
