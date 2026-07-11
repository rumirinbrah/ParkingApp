package com.jetnsync.parkingapp.feature_book.domain.model

data class ParkingSlot(
    val id: String = "",
    val bookedIntervals: List<TimeInterval> = emptyList()
)

data class TimeInterval(
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val reservationId: String = ""
)
