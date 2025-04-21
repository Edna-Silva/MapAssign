package com.example.hockeynamibiaorg.ui.coach

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hockeynamibiaorg.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// Data class for events
data class Event(
    val id: Int,
    val title: String,
    val date: LocalDate,
    val timeRange: String,
    val participants: List<Participant>,
    val progress: Int
)

data class Participant(
    val id: Int,
    val name: String,
    val avatarResId: Int
)

@Composable
fun EventsManagementScreen() {
    val events = remember {
        mutableStateListOf(
            Event(
                id = 1,
                title = "App Design",
                date = LocalDate.of(2022, 7, 11),
                timeRange = "07:00 - 1\n"+
                        "                    Participant(1, 2:00",
                participants = listOf("Sarah", R.drawable.avatar1),
                    Participant(2, "John", R.drawable.avatar2),
                    Participant(3, "Mike", R.drawable.avatar3),
                    Participant(4, "Anna", R.drawable.avatar4)
                ),
                progress = 50
            ),
            Event(
                id = 2,
                title = "Project Review",
                date = LocalDate.of(2022, 7, 15),
                timeRange = "13:00 - 15:00",
                participants = listOf(
                    Participant(1, "Sarah", R.drawable.avatar1),
                    Participant(2, "John", R.drawable.avatar2)
                ),
                progress = 25
            ),
            Event(
                id = 3,
                title = "Weekly Meeting",
                date = LocalDate.of(2022, 7, 18),
                timeRange = "10:00 - 11:30",
                participants = listOf(
                    Participant(3, "Mike", R.drawable.avatar3),
                    Participant(4, "Anna", R.drawable.avatar4)
                ),
                progress = 75
            )
        )
    }

    var showCalendarView by remember { mutableStateOf(true) }
    var showEventsList by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier.height(60.dp)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    selected = false,
                    onClick = { },
                    label = { Text("") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Tasks") },
                    selected = false,
                    onClick = { },
                    label = { Text("") }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Calendar",
                            tint = Color(0xFF8B80F8)
                        )
                    },
                    selected = true,
                    onClick = { },
                    label = { Text("") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { },
                    label = { Text("") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F4FB))
        ) {
            // Top app bar
            TopAppBar(
                title = {
                    Text(
                        text = "Calendar",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Go back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Show notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Main content
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Monthly Task Title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Monthly Task",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "July 02, 2022",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = "Add Task",
                            color = Color(0xFF8B80F8),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { /* Show add task dialog */ }
                        )
                    }

                    // Toggle buttons for view mode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                showCalendarView = true
                                showEventsList = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showCalendarView) Color(0xFF8B80F8) else Color.LightGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Calendar View")
                        }

                        Button(
                            onClick = {
                                showCalendarView = false
                                showEventsList = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showEventsList) Color(0xFF8B80F8) else Color.LightGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text("All Events")
                        }
                    }

                    if (showCalendarView) {
                        // Calendar View
                        CalendarView(
                            onDateSelected = { day ->
                                // Handle date selection
                            },
                            events = events
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Selected event details
                        events.firstOrNull { it.date == LocalDate.of(2022, 7, 11) }?.let { event ->
                            EventCard(
                                event = event,
                                onEditClick = { selectedEvent = event },
                                onDeleteClick = {
                                    eventToDelete = event
                                    showDeleteDialog = true
                                }
                            )
                        }
                    } else if (showEventsList) {
                        // All Events List
                        LazyColumn {
                            items(events) { event ->
                                EventCard(
                                    event = event,
                                    onEditClick = { selectedEvent = event },
                                    onDeleteClick = {
                                        eventToDelete = event
                                        showDeleteDialog = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Event Dialog
    if (selectedEvent != null) {
        EditEventDialog(
            event = selectedEvent!!,
            onDismiss = { selectedEvent = null },
            onSave = { updatedEvent ->
                val index = events.indexOfFirst { it.id == updatedEvent.id }
                if (index != -1) {
                    events[index] = updatedEvent
                }
                selectedEvent = null
            }
        )
    }

    // Delete Event Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                eventToDelete = null
            },
            title = { Text("Delete Event") },
            text = { Text("Are you sure you want to delete this event?") },
            confirmButton = {
                Button(
                    onClick = {
                        eventToDelete?.let { event ->
                            events.removeAll { it.id == event.id }
                        }
                        showDeleteDialog = false
                        eventToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        eventToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    onDateSelected: (LocalDate) -> Unit,
    events: List<Event>
) {
    val currentMonth = remember { YearMonth.of(2022, 7) }
    val daysInMonth = remember { currentMonth.lengthOfMonth() }
    val firstDayOfMonth = remember { currentMonth.atDay(1).dayOfWeek.value }

    Column {
        // Month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous month */ }) {
                Text("<", fontWeight = FontWeight.Bold)
            }

            Text(
                text = "July",
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { /* Next month */ }) {
                Text(">", fontWeight = FontWeight.Bold)
            }
        }

        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Calendar grid
        val totalCells = if (firstDayOfMonth > 1) daysInMonth + firstDayOfMonth - 1 else daysInMonth
        val rows = (totalCells + 6) / 7

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height((rows * 40).dp)
        ) {
            // Empty cells for days before the first day of month
            items(firstDayOfMonth - 1) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                )
            }

            // Days of current month
            items(daysInMonth) { day ->
                val date = LocalDate.of(2022, 7, day + 1)
                val hasEvent = events.any { it.date == date }
                val isSelected = day + 1 == 11 // Hardcoded selection for the example

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> Color(0xFF000000)
                                hasEvent -> Color.LightGray.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${day + 1}",
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }

            // Days of next month to fill the grid
            val remainingCells = rows * 7 - (firstDayOfMonth - 1 + daysInMonth)
            items(remainingCells) { day ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${day + 1}",
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${event.progress}%",
                    color = Color(0xFF8B80F8)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress indicator
            LinearProgressIndicator(
                progress = event.progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF8B80F8),
                trackColor = Color(0xFFE6E6FA)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Participants
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Participant avatars
                Row {
                    event.participants.take(4).forEachIndexed { index, participant ->
                        Box(
                            modifier = Modifier
                                .padding(end = if (index == event.participants.size - 1) 0.dp else (-12).dp)
                        ) {
                            Image(
                                painter = painterResource(id = participant.avatarResId),
                                contentDescription = participant.name,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Time range
                Text(
                    text = event.timeRange,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B80F8)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Update")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun EditEventDialog(
    event: Event,
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var timeRange by remember { mutableStateOf(event.timeRange) }
    var progress by remember { mutableStateOf(event.progress) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Event") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = timeRange,
                    onValueChange = { timeRange = it },
                    label = { Text("Time Range") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Progress: $progress%")
                Slider(
                    value = progress.toFloat(),
                    onValueChange = { progress = it.toInt() },
                    valueRange = 0f..100f,
                    steps = 20,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        event.copy(
                            title = title,
                            timeRange = timeRange,
                            progress = progress
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EventsManagementScreenPreview() {
    MaterialTheme {
        EventsManagementScreen()
    }
}