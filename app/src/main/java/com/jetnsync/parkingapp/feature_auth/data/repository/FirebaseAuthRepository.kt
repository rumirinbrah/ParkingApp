package com.jetnsync.parkingapp.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.jetnsync.parkingapp.feature_auth.domain.repository.AuthRepository
import com.jetnsync.parkingapp.feature_auth.domain.AuthError
import com.jetnsync.parkingapp.feature_book.domain.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


/**
 * FB auth
 *
 * @author zyzz
*/
class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<String?>
        get() = getUser()

    private fun getUser(): Flow<String?> {
        return callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.uid)
            }
            auth.addAuthStateListener(listener)

            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }
    }

    override suspend fun signIn(email: String , password: String): Result<Unit , AuthError> {
        return try {
            auth.signInWithEmailAndPassword(email , password).await()
            Result.Success(Unit)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.Error(
                AuthError.InvalidCredentials
            )
        } catch (e: Exception) {
            Result.Error(
                AuthError.Unknown(e.message ?: "Unknown error")
            )
        }
    }

    override suspend fun signUp(email: String , password: String): Result<Unit , AuthError> {
        return try {
            auth.createUserWithEmailAndPassword(email , password).await()
            Result.Success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(
                AuthError.UserAlreadyExists
            )
        } catch (e: Exception) {
            Result.Error(
                AuthError.Unknown(e.message ?: "Unknown error")
            )
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
