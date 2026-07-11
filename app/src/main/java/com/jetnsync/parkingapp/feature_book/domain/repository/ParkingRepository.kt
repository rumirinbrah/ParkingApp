package com.jetnsync.parkingapp.feature_book.domain.repository

import com.jetnsync.parkingapp.feature_book.domain.ParkingError
import com.jetnsync.parkingapp.feature_book.domain.Result
import com.jetnsync.parkingapp.feature_book.domain.model.Reservation
import kotlinx.coroutines.flow.Flow

interface ParkingRepository {
    fun getAvailableSlots(
        windowStart: Long,
        windowEnd: Long
    ): Flow<List<String>>

    fun getMyReservations(userId: String): Flow<List<Reservation>>

    suspend fun bookSlot(
        userId: String,
        slotId: String,
        startTime: Long,
        endTime: Long
    ): Result<Unit, ParkingError>

    suspend fun cancelReservation(reservationId: String): Result<Unit, ParkingError>
}
