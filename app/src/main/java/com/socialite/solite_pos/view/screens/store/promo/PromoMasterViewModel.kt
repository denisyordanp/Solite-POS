package com.socialite.solite_pos.view.screens.store.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.domain.domain.AddNewPromo
import com.socialite.domain.domain.GetPromos
import com.socialite.domain.domain.UpdatePromo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoMasterViewModel @Inject constructor(
    private val getPromos: GetPromos,
    private val addNewPromo: AddNewPromo,
    private val updatePromo: UpdatePromo,
) : ViewModel() {

    fun getPromos(status: Promo.Status) = getPromos.invoke(Promo.filter(status))

    fun insertPromo(data: Promo) {
        viewModelScope.launch {
            addNewPromo(data.asNewPromo())
        }
    }

    fun updatePromo(data: Promo) {
        viewModelScope.launch {
            updatePromo.invoke(data)
        }
    }
}
