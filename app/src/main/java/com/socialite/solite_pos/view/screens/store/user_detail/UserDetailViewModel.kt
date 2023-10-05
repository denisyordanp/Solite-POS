package com.socialite.solite_pos.view.screens.store.user_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.utility.state.DataState
import com.socialite.domain.domain.ChangePassword
import com.socialite.domain.domain.GetLoggedInUser
import com.socialite.domain.domain.UpdateUser
import com.socialite.solite_pos.schema.User
import com.socialite.solite_pos.utils.tools.mapper.toDomain
import com.socialite.solite_pos.utils.tools.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val updateUser: UpdateUser,
    private val getLoggedInUser: GetLoggedInUser,
    private val changePassword: ChangePassword
) : ViewModel() {

    private val _updateUserState = MutableStateFlow<DataState<Boolean>>(DataState.Idle)
    val updateUserState = _updateUserState.asStateFlow()

    private val _changePasswordState = MutableStateFlow<DataState<Boolean>>(DataState.Idle)
    val changePasswordState = _changePasswordState.asStateFlow()

    val user get() = getLoggedInUser().map { it.getDataOnly()?.toUi() }

    fun updateUser(user: User) = viewModelScope.launch {
        _updateUserState.emitAll(updateUser.invoke(user.toDomain(), true))
    }

    fun changePassword(oldPassword: String, newPassword: String) = viewModelScope.launch {
        _changePasswordState.emitAll(changePassword.invoke(oldPassword, newPassword))
    }
}