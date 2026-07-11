package com.jetnsync.parkingapp.feature_book.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jetnsync.parkingapp.feature_book.domain.ParkingError
import com.jetnsync.parkingapp.feature_book.domain.Result
import com.jetnsync.parkingapp.feature_book.domain.model.ParkingConstants
import com.jetnsync.parkingapp.feature_book.domain.model.ParkingSlot
import com.jetnsync.parkingapp.feature_book.domain.model.Reservation
import com.jetnsync.parkingapp.feature_book.domain.model.TimeInterval
import com.jetnsync.parkingapp.feature_book.domain.repository.ParkingRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseParkingRepository(
    private val firestore: FirebaseFirestore
) : ParkingRepository {

    override fun getAvailableSlots(
        windowStart: Long,
        windowEnd: Long
    ): Flow<List<String>> {
        require(windowStart <= windowEnd) {
            "windowStart must be before or equal to windowEnd."
        }

        return callbackFlow {
            val registration = firestore
                .collection("slots")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        cancel(
                            message = "Failed to observe slots.",
                            cause = exception
                        )
                        return@addSnapshotListener
                    }

                    val slots = snapshot?.toObjects(ParkingSlot::class.java)
                        ?.zip(snapshot.documents) { slot, document ->
                            slot.copy(id = document.id)
                        }
                        .orEmpty()

                    val existingSlotIds = slots.map { it.id }.toSet()
                    
                    val availableFromDb = slots.filter { slot ->
                        slot.bookedIntervals.none { interval ->
                            windowStart < interval.endTime && windowEnd > interval.startTime
                        }
                    }.map { it.id }

                    val neverBooked = ParkingConstants.ALL_SLOTS.filter { it !in existingSlotIds }

                    trySend((availableFromDb + neverBooked).sorted())
                }

            awaitClose {
                registration.remove()
            }
        }
    }

    override fun getMyReservations(
        userId: String
    ): Flow<List<Reservation>> = callbackFlow {

        val registration = firestore
            .collection("reservations")
            .whereEqualTo(Reservation::userId.name, userId)
            // Removed orderBy to avoid index requirement for first run
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    cancel(
                        message = "Failed to observe reservations.",
                        cause = exception
                    )
                    return@addSnapshotListener
                }

                val reservations = snapshot
                    ?.toObjects(Reservation::class.java)
                    ?.zip(snapshot.documents) { reservation, document ->
                        reservation.copy(id = document.id)
                    }
                    .orEmpty()
                    .sortedByDescending { it.startTime }

                trySend(reservations)
            }

        awaitClose {
            registration.remove()
        }
    }

    override suspend fun bookSlot(
        userId: String,
        slotId: String,
        startTime: Long,
        endTime: Long
    ): Result<Unit, ParkingError> {
        return try {
            firestore.runTransaction { transaction ->

                val slotRef = firestore
                    .collection("slots")
                    .document(slotId)

                val slot = transaction
                    .get(slotRef)
                    .toObject(ParkingSlot::class.java)
                    ?: ParkingSlot(id = slotId)

                val hasOverlap = slot.bookedIntervals.any { interval ->
                    startTime < interval.endTime &&
                            endTime > interval.startTime
                }

                if (hasOverlap) {
                    throw IllegalStateException("OVERLAP")
                }

                val reservationRef = firestore
                    .collection("reservations")
                    .document()

                val reservation = Reservation(
                    id = reservationRef.id,
                    slotId = slotId,
                    userId = userId,
                    startTime = startTime,
                    endTime = endTime
                )

                transaction.set(
                    slotRef,
                    slot.copy(
                        bookedIntervals = slot.bookedIntervals + TimeInterval(
                            startTime = startTime,
                            endTime = endTime,
                            reservationId = reservationRef.id
                        )
                    )
                )

                transaction.set(
                    reservationRef,
                    reservation
                )
            }.await()

            Result.Success(Unit)

        } catch (e: Exception) {
            when (e.message) {
                "OVERLAP" ->
                    Result.Error(ParkingError.OverlappingReservation)

                else ->
                    Result.Error(
                        ParkingError.Unknown(
                            e.message ?: "Booking failed."
                        )
                    )
            }
        }
    }

    override suspend fun cancelReservation(
        reservationId: String
    ): Result<Unit, ParkingError> {
        return try {
            firestore.runTransaction { transaction ->

                val reservationRef = firestore
                    .collection("reservations")
                    .document(reservationId)

                val reservation = transaction
                    .get(reservationRef)
                    .toObject(Reservation::class.java)
                    ?: throw IllegalStateException("Reservation not found.")

                val slotRef = firestore
                    .collection("slots")
                    .document(reservation.slotId)

                val slot = transaction
                    .get(slotRef)
                    .toObject(ParkingSlot::class.java)
                    ?: throw IllegalStateException("Parking slot not found.")

                transaction.update(
                    slotRef,
                    ParkingSlot::bookedIntervals.name,
                    slot.bookedIntervals.filter { it.reservationId != reservationId }
                )

                transaction.delete(reservationRef)
            }.await()

            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(
                ParkingError.Unknown(
                    e.message ?: "Failed to cancel reservation."
                )
            )
        }
    }
}
