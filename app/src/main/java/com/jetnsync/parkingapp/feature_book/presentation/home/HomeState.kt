package com.jetnsync.parkingapp.feature_book.presentation.home

import com.jetnsync.parkingapp.feature_book.domain.model.Reservation

/**
 * Home state
 * @author zyzz
 */
data class HomeState(
    val selectedStartTime: Long? = null,
    val selectedEndTime: Long? = null,
    val availableSlots: List<String> = emptyList(),
    val userReservations: List<Reservation> = emptyList(),
    val isLoading: Boolean = false,
    val isBookingInProgress: Boolean = false,
    val error: String? = null,
    val isTimePickerVisible: Boolean = false
)

/**
 * Home action
 * @author zyzz
 */
sealed class HomeAction {
    data object OnBookClick : HomeAction()
    data class OnTimeRangeSelected(val start: Long, val end: Long) : HomeAction()
    data class OnSlotSelected(val slotId: String) : HomeAction()
    data class OnCancelReservation(val reservationId: String) : HomeAction()
    data object OnDismissTimePicker : HomeAction()
    data object OnSignOut : HomeAction()
}
