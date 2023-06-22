package com.socialite.solite_pos.view.store.variant_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariantMasterViewModel @Inject constructor(
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
}
