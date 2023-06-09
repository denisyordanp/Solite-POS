package com.socialite.solite_pos.view.factory

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.socialite.solite_pos.utils.tools.ViewModelFactory

open class ViewModelFromFactory<T : ViewModel> {

    fun buildViewModel(activity: ComponentActivity, modelClass: Class<T>): T {
        return getProvider(activity)[modelClass]
    }

    private fun getProvider(activity: ComponentActivity): ViewModelProvider {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(activity, factory)
    }
}
