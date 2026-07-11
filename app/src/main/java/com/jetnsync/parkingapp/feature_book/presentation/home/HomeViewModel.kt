package com.jetnsync.parkingapp.feature_book.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetnsync.parkingapp.feature_auth.domain.AuthEvent
import com.jetnsync.parkingapp.feature_auth.domain.repository.AuthRepository
import com.jetnsync.parkingapp.feature_book.domain.ParkingError
import com.jetnsync.parkingapp.feature_book.domain.Result
import com.jetnsync.parkingapp.feature_book.domain.UIEvent
import com.jetnsync.parkingapp.feature_book.domain.repository.ParkingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Home view model
 * @author zyzz
 */
class HomeViewModel(
    private val parkingRepository: ParkingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()

    private var availableSlotsJob: Job? = null

    init {
        observeData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeData() {
        authRepository.currentUser
            .flatMapLatest { userId ->
                if (userId != null) {
                    parkingRepository.getMyReservations(userId)
                } else {
                    flowOf(emptyList())
                }
            }
            .onEach { reservations ->
                _state.update { it.copy(userReservations = reservations) }
            }
            .launchIn(viewModelScope)


//        _state.map { it.selectedStartTime to it.selectedEndTime }
//            .distinctUntilChanged()
//            .flatMapLatest { (start, end) ->
//                if (start != null && end != null) {
//                    parkingRepository.getAvailableSlots(start, end)
//                } else {
//                    flowOf(emptyList())
//                }
//            }
//            .onEach { slots ->
//                println("Slots are $slots")
//                _state.update { it.copy(availableSlots = slots) }
//            }
//            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnBookClick -> {
                showTimePicker()
            }
            is HomeAction.OnTimeRangeSelected -> {
                observeAvailableSlots(action.start, action.end)
            }
            is HomeAction.OnSlotSelected -> {
                bookSlot(action.slotId)
            }
            is HomeAction.OnCancelReservation -> {
                cancelReservation(action.reservationId)
            }
            HomeAction.OnDismissTimePicker -> {
                hideTimePicker()
            }
            HomeAction.OnSignOut -> {
                signOut()
            }
        }
    }

    private fun showTimePicker() {
        _state.update { it.copy(isTimePickerVisible = true) }
    }

    private fun hideTimePicker() {
        _state.update { it.copy(isTimePickerVisible = false) }
    }

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _events.send(UIEvent.LogOut)
        }
    }

    private fun observeAvailableSlots(
        startTime: Long,
        endTime: Long
    ) {
        _state.update {
            it.copy(
                selectedStartTime = startTime,
                selectedEndTime = endTime,
                isTimePickerVisible = false
            )
        }
        availableSlotsJob?.cancel()

        availableSlotsJob = parkingRepository
            .getAvailableSlots(startTime, endTime)
            .onEach { slots ->
                _state.update {
                    it.copy(availableSlots = slots)
                }
            }
            .launchIn(viewModelScope)
    }
    private fun bookSlot(slotId: String) {
        val start = _state.value.selectedStartTime ?: return
        val end = _state.value.selectedEndTime ?: return

        viewModelScope.launch {
            val userId = authRepository.currentUser.first() ?: return@launch

            _state.update { it.copy(isBookingInProgress = true, error = null) }

            val result = parkingRepository.bookSlot(
                userId = userId,
                slotId = slotId,
                startTime = start,
                endTime = end
            )

            when (result) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isBookingInProgress = false,
                            error = result.error.toUiError()
                        )
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isBookingInProgress = false,
                            selectedStartTime = null,
                            selectedEndTime = null
                        )
                    }
                }
            }
        }
    }

    private fun cancelReservation(reservationId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            parkingRepository.cancelReservation(reservationId)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun ParkingError.toUiError(): String {
        return when (this) {
            ParkingError.OverlappingReservation -> {
                "This slot is already booked for the selected time."
            }
            ParkingError.SlotAlreadyBooked -> {
                "Slot already booked."
            }
            ParkingError.Unauthorized -> {
                "You are not authorized to perform this action."
            }
            is ParkingError.Unknown -> {
                message
            }
        }
    }
}
