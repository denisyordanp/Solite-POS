package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.vo.Resource

class UserViewModel(private val repository: SoliteRepository) : ViewModel() {

    companion object : ViewModelFromFactory<UserViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): UserViewModel {
            return buildViewModel(activity, UserViewModel::class.java)
        }
    }

    fun getUser(userId: String): LiveData<Resource<User?>> {
        return repository.getUsers(userId)
    }
}
