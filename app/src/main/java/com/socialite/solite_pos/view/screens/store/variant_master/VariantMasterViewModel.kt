package com.socialite.solite_pos.view.screens.store.variant_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.AddNewVariant
import com.socialite.domain.domain.AddNewVariantOption
import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.domain.domain.UpdateVariant
import com.socialite.domain.domain.UpdateVariantOption
import com.socialite.domain.schema.main.Variant
import com.socialite.domain.schema.main.VariantOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariantMasterViewModel @Inject constructor(
    private val addNewVariant: AddNewVariant,
    private val updateVariant: UpdateVariant,
    private val addNewVariantOption: AddNewVariantOption,
    private val updateVariantOption: UpdateVariantOption,
    private val getVariantsWithOptions: GetVariantsWithOptions
) : ViewModel() {

    fun getVariants() = getVariantsWithOptions()

    fun insertVariant(data: Variant) {
        viewModelScope.launch {
            addNewVariant(data.asNewVariant())
        }
    }

    fun updateVariant(data: Variant) {
        viewModelScope.launch {
            updateVariant.invoke(data)
        }
    }

    fun insertVariantOption(data: VariantOption) {
        viewModelScope.launch {
            addNewVariantOption(data.asNewVariantOption())
        }
    }

    fun updateVariantOption(data: VariantOption) {
        viewModelScope.launch {
            updateVariantOption.invoke(data)
        }
    }
}
