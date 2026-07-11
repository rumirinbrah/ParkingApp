package com.jetnsync.parkingapp.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetnsync.parkingapp.feature_auth.domain.AuthEvent
import com.jetnsync.parkingapp.feature_auth.domain.repository.AuthRepository
import com.jetnsync.parkingapp.feature_auth.domain.toUiError
import com.jetnsync.parkingapp.feature_book.domain.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> {
                onEmailChange(action.email)
            }

            is LoginAction.OnPasswordChange -> {
                onPwdChange(action.password)
            }

            LoginAction.OnLoginClick -> {
                login()
            }
        }
    }

    private fun onEmailChange(text: String) {
        _state.update { it.copy(email = text) }
    }

    private fun onPwdChange(text: String) {
        _state.update { it.copy(password = text) }
    }

    private fun login() {
        if (state.value.email.isBlank() || state.value.password.isBlank()) {
            _state.update {
                it.copy(
                    error = "Email and password cannot be empty"
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true ,
                    error = null
                )
            }
            val result = authRepository.signIn(
                email = state.value.email ,
                password = state.value.password
            )
            when (result) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false ,
                            error = result.error.toUiError()
                        )
                    }
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false , isSuccess = true)
                    }
                    _events.send(AuthEvent.Success)
                }
            }
        }
    }
}
