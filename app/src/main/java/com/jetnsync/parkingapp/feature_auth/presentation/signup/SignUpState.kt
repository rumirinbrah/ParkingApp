package com.jetnsync.parkingapp.feature_auth.presentation.signup

import com.jetnsync.parkingapp.feature_auth.domain.AuthError

/**
 * Sign up state
 * @author zyzz
 */
data class SignUpState(
    val email: String = "" ,
    val password: String = "" ,
    val confirmPassword: String = "" ,
    val isLoading: Boolean = false ,
    val error: String? = null ,
    val isSuccess: Boolean = false
)

/**
 * Sign up action
 * @author zyzz
 */
sealed class SignUpAction {
    data class OnEmailChange(val email: String) : SignUpAction()
    data class OnPasswordChange(val password: String) : SignUpAction()
    data class OnConfirmPasswordChange(val password: String) : SignUpAction()
    data object OnSignUpClick : SignUpAction()
}
