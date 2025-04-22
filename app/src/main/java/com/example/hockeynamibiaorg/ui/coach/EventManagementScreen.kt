package com.example.hockeynamibiaorg.ui.coach

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// Data classes for events
data class Event(
    val id: String,
    val title: String,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val progress: Int,
    val description: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventManagementScreen(navController: NavController) {
    // State variables
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showAddEventDialog by remember { mutableStateOf(false) }
    var showEventDetails by remember { mutableStateOf(false) }

    // Sample events
    val events = remember {
        mutableStateListOf(
            Event(
                id = "1",
                title = "App Design",
                date = LocalDate.of(2022, 7, 2),
                startTime = "07:00",
                endTime = "12:00",
                progress = 50,
                description = "Design the new hockey app interface"
            ),
            Event(
                id = "2",
                title = "Team Meeting",
                date = LocalDate.of(2022, 7, 15),
                startTime = "14:00",
                endTime = "16:00",
                progress = 20,
                description = "Discuss tournament strategy"
            )
        )
    }

    // Main screen content
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            item {
                // Header section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calendar",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = { showAddEventDialog = true },
                        modifier = Modifier.size(height = 40.dp, width = 120.dp)
                    ) {
                        Text("Add Event")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Month/year display with navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous month")
                    }

                    Text(
                        text = "Monthly Tasks\n${currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Next month",
                            modifier = Modifier.rotate(180f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Weekday headers
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar days grid
                val daysInMonth = currentMonth.lengthOfMonth()
                val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value
                val days = List(6 * 7) { index ->
                    if (index >= firstDayOfMonth - 1 && index < firstDayOfMonth - 1 + daysInMonth) {
                        index - firstDayOfMonth + 2
                    } else {
                        null
                    }
                }.chunked(7)

                days.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { day ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        day?.let {
                                            selectedDate = currentMonth.atDay(it)
                                            val eventForDay = events.find { it.date == selectedDate }
                                            if (eventForDay != null) {
                                                selectedEvent = eventForDay
                                                showEventDetails = true
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                day?.let {
                                    val date = currentMonth.atDay(it)
                                    val hasEvent = events.any { event -> event.date == date }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = it.toString(),
                                            color = when {
                                                date == selectedDate -> Color.White
                                                date == LocalDate.now() -> MaterialTheme.colorScheme.primary
                                                else -> Color.Black
                                            },
                                            modifier = Modifier
                                                .background(
                                                    if (date == selectedDate) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    RoundedCornerShape(50)
                                                )
                                                .size(32.dp)
                                                .padding(8.dp),
                                            textAlign = TextAlign.Center
                                        )
                                        if (hasEvent) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Events for selected date
                val dayEvents = events.filter { it.date == selectedDate }
                if (dayEvents.isNotEmpty()) {
                    dayEvents.forEach { event ->
                        EventCard(
                            event = event,
                            onCardClick = {
                                selectedEvent = event
                                showEventDetails = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Text(
                        text = "No events for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // Add Event Dialog
    if (showAddEventDialog) {
        AddEventDialog(
            onDismiss = { showAddEventDialog = false },
            onConfirm = { newEvent ->
                events.add(newEvent)
                showAddEventDialog = false
            },
            initialDate = selectedDate
        )
    }

    // Event Details Dialog
    if (showEventDetails && selectedEvent != null) {
        EventDetailsDialog(
            event = selectedEvent!!,
            onDismiss = { showEventDetails = false },
            onDelete = { event ->
                events.remove(event)
                showEventDetails = false
            },
            onUpdate = { updatedEvent ->
                val index = events.indexOfFirst { it.id == updatedEvent.id }
                if (index != -1) {
                    events[index] = updatedEvent
                }
                showEventDetails = false
            }
        )
    }
}

@Composable
fun EventCard(event: Event, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${event.startTime} - ${event.endTime}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${event.progress}% complete")
            LinearProgressIndicator(
                progress = event.progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (Event) -> Unit,
    initialDate: LocalDate
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(initialDate) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Event") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Add date picker, time pickers, and other fields here
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = progress,
                    onValueChange = { progress = it },
                    label = { Text("Progress (0-100)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newEvent = Event(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        progress = progress.toIntOrNull() ?: 0,
                        description = description
                    )
                    onConfirm(newEvent)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EventDetailsDialog(
    event: Event,
    onDismiss: () -> Unit,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedEvent by remember { mutableStateOf(event) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edit Event" else "Event Details") },
        text = {
            if (isEditing) {
                Column {
                    OutlinedTextField(
                        value = editedEvent.title,
                        onValueChange = { editedEvent = editedEvent.copy(title = it) },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Add other editable fields similarly
                }
            } else {
                Column {
                    Text("Title: ${event.title}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Date: ${event.date}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Time: ${event.startTime} - ${event.endTime}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Progress: ${event.progress}%")
                    LinearProgressIndicator(
                        progress = event.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Description: ${event.description}")
                }
            }
        },
        confirmButton = {
            if (isEditing) {
                Button(onClick = {
                    onUpdate(editedEvent)
                    isEditing = false
                }) {
                    Text("Save")
                }
            } else {
                Row {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onDelete(event) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEditing) "Cancel" else "Close")
            }
        }
    )
}