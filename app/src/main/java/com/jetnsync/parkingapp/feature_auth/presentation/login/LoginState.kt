package com.jetnsync.parkingapp.feature_auth.presentation.login



data class LoginState(
    val email: String = "" ,
    val password: String = "" ,
    val isLoading: Boolean = false ,
    val error: String? = null ,
    val isSuccess: Boolean = false
)

sealed class LoginAction {
    data class OnEmailChange(val email: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    data object OnLoginClick : LoginAction()
}
