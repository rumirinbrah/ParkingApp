package com.jetnsync.parkingapp.feature_auth.domain.repository

import com.jetnsync.parkingapp.feature_auth.domain.AuthError
import com.jetnsync.parkingapp.feature_book.domain.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<String?> // Returns UID if logged in

    suspend fun signIn(email: String, password: String): Result<Unit, AuthError>
    suspend fun signUp(email: String, password: String): Result<Unit, AuthError>
    suspend fun signOut()
}
