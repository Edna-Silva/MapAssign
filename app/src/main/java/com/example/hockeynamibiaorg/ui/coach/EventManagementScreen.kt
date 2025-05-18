package com.example.hockeynamibiaorg.ui.coach

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.Event
import com.example.hockeynamibiaorg.data.viewModels.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.hockeynamibiaorg.ui.theme.LighterBlue
import com.example.hockeynamibiaorg.ui.theme.DarkBlue
import com.example.hockeynamibiaorg.ui.theme.BlueAccent
import com.example.hockeynamibiaorg.ui.theme.GoldYellow


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventManagementScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    // State variables
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showAddEventDialog by remember { mutableStateOf(false) }
    var showEventDetails by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val events by eventViewModel.events.collectAsState()

    // Filter events based on search and filter
    val filteredEvents = remember(events, searchQuery, selectedFilter) {
        events.filter { event ->
            val matchesType = selectedFilter == "ALL" || event.type.equals(selectedFilter, ignoreCase = true)
            val matchesSearch = if (searchQuery.isNotEmpty()) {
                event.title.contains(searchQuery, ignoreCase = true) ||
                        event.description.contains(searchQuery, ignoreCase = true)
            } else {
                true
            }
            matchesType && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Event Management",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF142143),
                    navigationIconContentColor = Color(0xFF142143)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text("Search events...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    singleLine = true
                )

                // Filter chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("ALL", "TOURNAMENT", "CAMP", "LEAGUE")) { type ->
                        FilterChip(
                            modifier = Modifier.widthIn(min = 80.dp),
                            selected = selectedFilter == type,
                            onClick = { selectedFilter = type },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFFAF00),
                                selectedLabelColor = Color(0xFF142143),
                                containerColor = Color(0xFFE4E4E4),
                                labelColor = Color(0xFF142143)
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF142143), Color(0xFF3F5291))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "All Events",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Manage hockey events in Namibia",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "All Events (${filteredEvents.size})",
                        color = Color(0xFF142143),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Button(
                        onClick = { showAddEventDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A5D94),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Event")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredEvents.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredEvents.sortedBy { it.date }) { event ->
                            ModernEventCard(
                                event = event,
                                onCardClick = {
                                    selectedEvent = event
                                    showEventDetails = true
                                },
                                isManagement = true
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No events found",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    // Add Event Dialog
    if (showAddEventDialog) {
        AddEventDialog(
            onDismiss = { showAddEventDialog = false },
            onConfirm = { newEvent ->
                eventViewModel.addEvent(newEvent)
                showAddEventDialog = false
            },
            initialDate = LocalDate.now()
        )
    }

    // Event Details Dialog
    if (showEventDetails && selectedEvent != null) {
        EventDetailsDialog(
            event = selectedEvent!!,
            onDismiss = { showEventDetails = false },
            onDelete = { event ->
                eventViewModel.deleteEvent(event.id)
                showEventDetails = false
            },
            onUpdate = { updatedEvent ->
                eventViewModel.updateEvent(updatedEvent)
                showEventDetails = false
            }
        )
    }
}
@Composable
fun ModernEventCard(
    event: Event,
    onCardClick: () -> Unit,
    isManagement: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Main event information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date indicator with gradient background
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFFFAF00), Color(0xFFFFAF00).copy(alpha = 0.7f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val dateParts = event.date.split("-")
                        Text(
                            text = dateParts.getOrNull(2) ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF142143)
                        )
                        Text(
                            text = dateParts.getOrNull(1) ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF142143).copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF142143)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF1A5D94),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${event.date} at ${event.time}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF1A5D94),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.location,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // View button (now toggles expanded state)
                if (isManagement) {
                    OutlinedButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.padding(start = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF1A5D94)
                        )
                    ) {
                        Text(
                            text = if (expanded) "Hide" else "View",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Expanded details section
            if (isManagement) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Divider(color = Color(0xFFE4E4E4))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF1A5D94),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = event.description,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Event type chip
                        Surface(
                            modifier = Modifier.padding(bottom = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = when(event.type) {
                                "TOURNAMENT" -> Color(0xFF2A6DA4).copy(alpha = 0.1f)
                                "CAMP" -> Color(0xFFFFAF00).copy(alpha = 0.1f)
                                "LEAGUE" -> Color(0xFF142143).copy(alpha = 0.1f)
                                else -> Color(0xFFE4E4E4)
                            }
                        ) {
                            Text(
                                text = "Type: ${event.type.lowercase().replaceFirstChar { it.uppercase() }}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = when(event.type) {
                                    "TOURNAMENT" -> Color(0xFF2A6DA4)
                                    "CAMP" -> Color(0xFF142143)
                                    "LEAGUE" -> Color(0xFF142143)
                                    else -> Color.Gray
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }


                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (Event) -> Unit,
    initialDate: LocalDate
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(initialDate.format(DateTimeFormatter.ISO_DATE)) }
    var time by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var expandedType by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add New Event",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF142143),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (HH:MM)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedType,
                    onExpandedChange = { expandedType = !expandedType }
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Event Type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF1A5D94),
                            unfocusedBorderColor = Color(0xFFE4E4E4),
                            cursorColor = Color(0xFF1A5D94)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        listOf("TOURNAMENT", "CAMP", "LEAGUE").forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    type = selectionOption
                                    expandedType = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1A5D94),
                        unfocusedBorderColor = Color(0xFFE4E4E4),
                        cursorColor = Color(0xFF1A5D94)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF1A5D94)
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val newEvent = Event(
                                id = "",
                                teamId = "",
                                title = title,
                                description = description,
                                date = date,
                                time = time,
                                type = type,
                                location = location
                            )
                            onConfirm(newEvent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A5D94),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Event")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsDialog(
    event: Event,
    onDismiss: () -> Unit,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedEvent by remember { mutableStateOf(event) }
    var expandedType by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (isEditing) "Edit Event" else "Event Details",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF142143),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isEditing) {
                    Column {
                        OutlinedTextField(
                            value = editedEvent.title,
                            onValueChange = { editedEvent = editedEvent.copy(title = it) },
                            label = { Text("Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A5D94),
                                unfocusedBorderColor = Color(0xFFE4E4E4),
                                cursorColor = Color(0xFF1A5D94)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = editedEvent.date,
                            onValueChange = { editedEvent = editedEvent.copy(date = it) },
                            label = { Text("Date (YYYY-MM-DD)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A5D94),
                                unfocusedBorderColor = Color(0xFFE4E4E4),
                                cursorColor = Color(0xFF1A5D94)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = editedEvent.time,
                            onValueChange = { editedEvent = editedEvent.copy(time = it) },
                            label = { Text("Time (HH:MM)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A5D94),
                                unfocusedBorderColor = Color(0xFFE4E4E4),
                                cursorColor = Color(0xFF1A5D94)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedType,
                            onExpandedChange = { expandedType = !expandedType }
                        ) {
                            OutlinedTextField(
                                value = editedEvent.type,
                                onValueChange = { editedEvent = editedEvent.copy(type = it) },
                                label = { Text("Event Type") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .menuAnchor(), // Added the missing modifier

                            readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType)
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF1A5D94),
                                    unfocusedBorderColor = Color(0xFFE4E4E4),
                                    cursorColor = Color(0xFF1A5D94)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = expandedType,
                                onDismissRequest = { expandedType = false }
                            ) {
                                listOf("TOURNAMENT", "CAMP", "LEAGUE").forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            editedEvent = editedEvent.copy(type = selectionOption)
                                            expandedType = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = editedEvent.location,
                            onValueChange = { editedEvent = editedEvent.copy(location = it) },
                            label = { Text("Location") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A5D94),
                                unfocusedBorderColor = Color(0xFFE4E4E4),
                                cursorColor = Color(0xFF1A5D94)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = editedEvent.description,
                            onValueChange = { editedEvent = editedEvent.copy(description = it) },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF1A5D94),
                                unfocusedBorderColor = Color(0xFFE4E4E4),
                                cursorColor = Color(0xFF1A5D94)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 3
                        )
                    }
                } else {
                    Column {
                        Text(
                            text = editedEvent.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF142143),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF1A5D94),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${editedEvent.date} at ${editedEvent.time}",
                                color = Color(0xFF142143).copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF1A5D94),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = editedEvent.location,
                                color = Color(0xFF142143).copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Event type chip
                        Surface(
                            modifier = Modifier.padding(bottom = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = when(editedEvent.type) {
                                "TOURNAMENT" -> Color(0xFF2A6DA4).copy(alpha = 0.1f)
                                "CAMP" -> Color(0xFFFFAF00).copy(alpha = 0.1f)
                                "LEAGUE" -> Color(0xFF142143).copy(alpha = 0.1f)
                                else -> Color(0xFFE4E4E4)
                            }
                        ) {
                            Text(
                                text = "Type: ${editedEvent.type.lowercase().replaceFirstChar { it.uppercase() }}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = when(editedEvent.type) {
                                    "TOURNAMENT" -> Color(0xFF2A6DA4)
                                    "CAMP" -> Color(0xFF142143)
                                    "LEAGUE" -> Color(0xFF142143)
                                    else -> Color.Gray
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Divider(color = Color(0xFFE4E4E4), modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = editedEvent.description,
                            color = Color(0xFF142143).copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isEditing) {
                        TextButton(
                            onClick = { isEditing = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF1A5D94)
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                onUpdate(editedEvent)
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1A5D94),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save")
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onDelete(event) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 1.dp,
                                //color = Color.Red
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { isEditing = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1A5D94),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}