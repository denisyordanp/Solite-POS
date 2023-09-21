package com.socialite.solite_pos.view.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.state.DataState
import com.socialite.domain.domain.LoginUser
import com.socialite.domain.domain.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUser: LoginUser,
    private val registerUser: RegisterUser,
) : ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _viewState.emitAll(
            loginUser(email, password)
                .map {
                    when (it) {
                        is DataState.Error -> _viewState.value.copyError(it.errorState)
                        DataState.Loading -> _viewState.value.copyLoading()
                        is DataState.Success -> _viewState.value.copySucceed()
                        else -> viewState.value
                    }
                }
        )
    }

    fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ) =  viewModelScope.launch {
        _viewState.emitAll(
            registerUser(name, email, password, storeName)
                .map {
                    when (it) {
                        is DataState.Error -> _viewState.value.copyError(it.errorState)
                        DataState.Loading -> _viewState.value.copyLoading()
                        is DataState.Success -> _viewState.value.copySucceed()
                        else -> viewState.value
                    }
                }
        )
    }

    fun resetState() {
        viewModelScope.launch {
            _viewState.emit(LoginViewState.idle())
        }
    }
}
