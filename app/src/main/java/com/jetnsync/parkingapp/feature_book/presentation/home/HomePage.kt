package com.jetnsync.parkingapp.feature_book.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetnsync.parkingapp.core.presentation.VerticalSpace
import com.jetnsync.parkingapp.feature_auth.domain.AuthEvent
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpPage
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpState
import com.jetnsync.parkingapp.feature_book.domain.UIEvent
import com.jetnsync.parkingapp.feature_book.domain.model.ParkingConstants
import com.jetnsync.parkingapp.feature_book.domain.model.Reservation
import com.jetnsync.parkingapp.ui.theme.ParkingAppTheme
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun HomeRoot(
    modifier: Modifier = Modifier,
    logout : ()->Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                UIEvent.LogOut -> {
                    logout()
                }
            }
        }
    }
    HomePage(
        state,
        onAction = {
            viewModel.onAction(it)
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "ParkSpot" ,
                    style = MaterialTheme.typography.headlineSmall ,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        onAction(HomeAction.OnSignOut)
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp ,
                        contentDescription = "Sign Out"
                    )
                }
            }
            //-------------Main Content Area-------------

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (state.selectedStartTime == null || state.selectedEndTime == null) {
                    IdleContent(onAction)
                } else {
                    BookingContent(state, onAction)
                }
            }
            //-------------reservations-------------
            HorizontalDivider()
            UserReservationsSection(state.userReservations, onAction)
        }
        //-------------picker overlay-------------
        if (state.isTimePickerVisible) {
            TimeRangePickerDialog(
                onDismiss = { onAction(HomeAction.OnDismissTimePicker) },
                onConfirm = { start, end ->
                    onAction(HomeAction.OnTimeRangeSelected(start, end))
                },
                initialStart = state.selectedStartTime,
                initialEnd = state.selectedEndTime
            )
        }
    }


}

@Composable
private fun IdleContent(
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        VerticalSpace(24.dp)
        Text(
            text = "Welcome to ParkSpot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        VerticalSpace(8.dp)
        Text(
            text = "Book a slot by selecting your preferred time window.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        VerticalSpace(32.dp)
        Button(
            onClick = { onAction(HomeAction.OnBookClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Select Time & Book Slot", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun BookingContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    val selectedWindow = remember(
        state.selectedStartTime,
        state.selectedEndTime
    ) {
        if (state.selectedStartTime == null || state.selectedEndTime == null) {
            "Select a time window"
        } else {
            val start = timeFormatter.format(Date(state.selectedStartTime))
            val end = timeFormatter.format(Date(state.selectedEndTime))
            "$start - $end"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Time window indicator
        Card(
            onClick = {
                onAction(HomeAction.OnBookClick)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = "Selected Window",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = selectedWindow,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Change",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        VerticalSpace(24.dp)
        
        Text(
            text = "Available Slots",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        VerticalSpace(12.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ParkingConstants.ALL_SLOTS) { slotId ->

                SlotCard(
                    slotId = slotId,
                    isAvailable = state.availableSlots.contains(slotId),
                    onClick = {
                        onAction(HomeAction.OnSlotSelected(slotId))
                    }
                )
            }
        }
    }
}

@Composable
private fun SlotCard(
    slotId: String,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isAvailable) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                }
            )
            .clickable(
                enabled = isAvailable ,
                onClick = {
                    if(isAvailable){
                        onClick()
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = slotId,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (!isAvailable) {
                Text(
                    text = "Occupied",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun UserReservationsSection(
    reservations: List<Reservation>,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Your Upcoming Bookings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        VerticalSpace(8.dp)

        if (reservations.isEmpty()) {
            Text(
                text = "No active reservations.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            return@Column
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = reservations,
                key = { it.id }
            ) { reservation ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = "Slot ${reservation.slotId}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = reservation.startTime.toFormattedDateTime(),
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                text = reservation.endTime.toFormattedDateTime(),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        IconButton(
                            onClick = {
                                onAction(
                                    HomeAction.OnCancelReservation(
                                        reservation.id
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Cancel Reservation",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
private fun Long.toFormattedDateTime() : String{
    val formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")

    val formatted = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
    return formatted
}

@Preview(showBackground = true)
@Composable
private fun HomePagePreview() {
    ParkingAppTheme {
        Box(Modifier.fillMaxSize()) {
            HomePage(
                HomeState()
            ) { }
        }
    }
}
