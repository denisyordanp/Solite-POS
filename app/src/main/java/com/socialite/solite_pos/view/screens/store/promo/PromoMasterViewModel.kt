package com.socialite.solite_pos.view.screens.store.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.data.repository.PromosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoMasterViewModel @Inject constructor(
    private val promosRepository: PromosRepository,
) : ViewModel() {

    fun getPromos(status: Promo.Status): Flow<List<Promo>> {
        return promosRepository.getPromos(Promo.filter(status))
    }

    fun insertPromo(data: Promo) {
        viewModelScope.launch {
            promosRepository.insertPromo(data.asNewPromo())
        }
    }

    fun updatePromo(data: Promo) {
        viewModelScope.launch {
            promosRepository.updatePromo(data)
        }
    }
}
