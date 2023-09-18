package com.socialite.solite_pos.view.screens.store.store_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.state.DataState
import com.socialite.domain.domain.AddNewUser
import com.socialite.domain.domain.GetUsers
import com.socialite.domain.domain.UpdateUser
import com.socialite.domain.schema.main.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreUsersViewModel @Inject constructor(
    private val getUsers: GetUsers,
    private val addNewUser: AddNewUser,
    private val updateUser: UpdateUser
) : ViewModel() {

    private val _actionUserFLow = MutableSharedFlow<DataState<Boolean>>()
    val actionUserFLow = _actionUserFLow.asSharedFlow()

    private val _usersFLow = MutableStateFlow<DataState<List<User>>>(DataState.Idle)
    val usersFLow = _usersFLow.asStateFlow()

    fun loadUsers() = viewModelScope.launch {
        _usersFLow.emitAll(getUsers())
    }

    fun submitUser(user: User) = viewModelScope.launch {
        if (user.isNewUser) {
            _actionUserFLow.emitAll(addNewUser(user))
        } else {
            _actionUserFLow.emitAll(updateUser(user))
        }
    }

    fun resetActionState() = viewModelScope.launch {
        _actionUserFLow.emit(DataState.Idle)
    }
}