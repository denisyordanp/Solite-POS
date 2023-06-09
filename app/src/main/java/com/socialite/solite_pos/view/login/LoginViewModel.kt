package com.socialite.solite_pos.view.login

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.view.factory.ViewModelFromFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUser: LoginUser,
    private val registerUser: RegisterUser,
) : ViewModel() {

    companion object : ViewModelFromFactory<LoginViewModel>() {
        fun getOrderViewModel(activity: ComponentActivity): LoginViewModel {
            return buildViewModel(activity, LoginViewModel::class.java)
        }
    }

    private val _viewState = MutableStateFlow(LoginViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            val currentState = _viewState.value

            flow {
                val isSuccessLogin = loginUser(email, password)
                if (isSuccessLogin) emit(currentState.copySucceed())
            }.onStart {
                emit(currentState.copyLoading())
            }.catch {
                emit(currentState.copyError(it.message ?: ""))
            }.collect(_viewState)
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ) {
        viewModelScope.launch {
            val currentState = _viewState.value

            flow {
                val isSuccessRegister = registerUser(name, email, password, storeName)
                if (isSuccessRegister) emit(currentState.copySucceed())
            }.onStart {
                emit(currentState.copyLoading())
            }.catch {
                emit(currentState.copyError(it.message ?: ""))
            }.collect(_viewState)
        }
    }

    fun resetState() {
        _viewState.value = LoginViewState.idle()
    }
}
