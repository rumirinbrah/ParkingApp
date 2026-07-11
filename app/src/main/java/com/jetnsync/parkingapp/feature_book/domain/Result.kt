package com.jetnsync.parkingapp.feature_book.domain

typealias DomainError = Error

sealed class NetworkError : Error {
    data class CustomError(val error : String) : NetworkError()
}


sealed class ParkingError : Error {
    data object SlotAlreadyBooked : ParkingError()
    data object OverlappingReservation : ParkingError()
    data object Unauthorized : ParkingError()
    data class Unknown(val message: String) : ParkingError()
}

sealed interface Result<out D , out E : DomainError> {
    data class Success<out D>(val data: D) : Result<D , Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing , E>
}

inline fun <T , E : DomainError , R> Result<T , E>.map(map: (T) -> R): Result<R , E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}
