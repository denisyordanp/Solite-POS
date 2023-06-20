package com.socialite.solite_pos.view.store.promo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PromoMasterViewModel(
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PromoMasterViewModel(
                    promosRepository = LoggedInRepositoryInjection.providePromosRepository(context)
                ) as T
            }
        }
    }
}
