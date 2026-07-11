package com.jetnsync.parkingapp.core.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen{
    @Serializable
    data object Auth : Screen()

    @Serializable
    data object Home : Screen()
}