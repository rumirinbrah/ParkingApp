package com.jetnsync.parkingapp.feature_auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetnsync.parkingapp.feature_auth.domain.AuthError
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

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()


    init {
        observeUser()
    }

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnEmailChange -> {
                onEmailChange(action.email)
            }

            is SignUpAction.OnPasswordChange -> {
                onPasswordChange(action.password)
            }

            is SignUpAction.OnConfirmPasswordChange -> {
                onConfirmPasswordChange(action.password)
            }

            SignUpAction.OnSignUpClick -> {
                signUp()
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            authRepository.currentUser.collect {
                it?.let {
                    _events.send(AuthEvent.Success)
                }
            }
        }
    }

    private fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmPassword = password) }
    }

    private fun signUp() {
        if (state.value.password != state.value.confirmPassword) {
            _state.update {
                it.copy(
                    isLoading = true ,
                    error = "Passwords dont match"
                )
            }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true , error = null) }
            val result = authRepository.signUp(
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
                    _state.update { it.copy(isLoading = false , isSuccess = true) }
                    _events.send(AuthEvent.Success)
                }
            }
        }
    }
}
