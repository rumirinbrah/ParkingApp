package com.jetnsync.parkingapp.feature_auth.domain

import com.jetnsync.parkingapp.feature_book.domain.Error

sealed class AuthError : Error {
    data object InvalidCredentials : AuthError()
    data object UserNotFound : AuthError()
    data object UserAlreadyExists : AuthError()
    data class Unknown(val message: String) : AuthError()
}

fun AuthError.toUiError(): String {
    return when (this) {
        AuthError.InvalidCredentials -> "Invalid email or password."
        AuthError.UserNotFound -> "User not found."
        AuthError.UserAlreadyExists -> "An account with this email already exists."
        is AuthError.Unknown -> message
    }
}
