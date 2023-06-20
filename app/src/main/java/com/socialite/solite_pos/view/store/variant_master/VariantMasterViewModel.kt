package com.socialite.solite_pos.view.store.variant_master

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class VariantMasterViewModel(
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val getVariantsWithOptions: GetVariantsWithOptions
) : ViewModel() {

    fun getVariants() = getVariantsWithOptions()

    fun insertVariant(data: Variant) {
        viewModelScope.launch {
            variantsRepository.insertVariant(data.asNewVariant())
        }
    }

    fun updateVariant(data: Variant) {
        viewModelScope.launch {
            variantsRepository.updateVariant(data)
        }
    }

    fun insertVariantOption(data: VariantOption) {
        viewModelScope.launch {
            variantOptionsRepository.insertVariantOption(data.asNewVariantOption())
        }
    }

    fun updateVariantOption(data: VariantOption) {
        viewModelScope.launch {
            variantOptionsRepository.updateVariantOption(data)
        }
    }

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VariantMasterViewModel(
                    variantsRepository = LoggedInRepositoryInjection.provideVariantsRepository(
                        context
                    ),
                    variantOptionsRepository = LoggedInRepositoryInjection.provideVariantOptionsRepository(
                        context
                    ),
                    getVariantsWithOptions = LoggedInDomainInjection.provideGetVariantsWithOptions(context)
                ) as T
            }
        }
    }
}
