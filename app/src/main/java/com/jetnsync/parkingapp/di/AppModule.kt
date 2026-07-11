package com.jetnsync.parkingapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jetnsync.parkingapp.feature_auth.data.repository.FirebaseAuthRepository
import com.jetnsync.parkingapp.feature_auth.domain.repository.AuthRepository
import com.jetnsync.parkingapp.feature_auth.presentation.login.LoginViewModel
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpViewModel
import com.jetnsync.parkingapp.feature_book.data.repository.FirebaseParkingRepository
import com.jetnsync.parkingapp.feature_book.domain.repository.ParkingRepository
import com.jetnsync.parkingapp.feature_book.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Repositories
    single<AuthRepository> { FirebaseAuthRepository(get()) }
    single<ParkingRepository> { FirebaseParkingRepository(get()) }
    
    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel {
        HomeViewModel(
            parkingRepository = get(),
            authRepository = get()
        )
    }
}
