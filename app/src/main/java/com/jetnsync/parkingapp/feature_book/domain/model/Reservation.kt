package com.jetnsync.parkingapp.feature_book.domain.model

/**
 * res
 * @author zyzz
*/
data class Reservation(
    val id: String = "",
    val slotId: String = "",
    val userId: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L
)
