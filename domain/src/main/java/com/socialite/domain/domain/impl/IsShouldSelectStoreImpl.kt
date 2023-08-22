package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.domain.domain.IsShouldSelectStore
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsShouldSelectStoreImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val storeRepository: StoreRepository,
    private val getProductWithCategories: GetProductWithCategories,
) : IsShouldSelectStore {
    override fun invoke(): Flow<Boolean> {
        return flow {
            val selectedStore = settingRepository.getNewSelectedStore().first()
            val products = getProductWithCategories().first()
            val stores = storeRepository.getStores().first()
            if (selectedStore.isEmpty() && products.isNotEmpty()) {
                emit(true)
            } else if (selectedStore.isNotEmpty()) {
                if (stores.any { it.id == selectedStore }) {
                    emit(false)
                } else {
                    emit(true)
                }
            } else {
                emit(false)
            }
        }
    }
}