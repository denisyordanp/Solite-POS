package com.socialite.solite_pos.viewmodelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.utils.di.Injection.provideSoliteRepository
import com.socialite.solite_pos.view.viewmodel.MainViewModel

class ViewModelFactory private constructor(private val repository: SoliteRepository) : NewInstanceFactory() {
	companion object {
		@Volatile
		private var INSTANCE: ViewModelFactory? = null

		fun getInstance(context: Context?): ViewModelFactory {
			if (INSTANCE == null) {
				synchronized(ViewModelFactory::class.java) {
					if (INSTANCE == null) {
						INSTANCE = ViewModelFactory(
								provideSoliteRepository(context!!)
						)
					}
				}
			}
			return INSTANCE!!
		}
	}

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
			return MainViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
	}
}
