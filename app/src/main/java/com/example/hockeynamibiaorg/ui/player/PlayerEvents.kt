package com.example.hockeynamibiaorg.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.Event
import com.example.hockeynamibiaorg.data.viewModels.EventViewModelNew

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEntriesScreen(navController: NavController, eventViewModel: EventViewModelNew = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var filteredEvents by remember { mutableStateOf(emptyList<Event>()) }

    val events by eventViewModel.events.collectAsState()

    // Update filtered events when filter changes or search query changes
    LaunchedEffect(selectedFilter, searchQuery, events) {
        filteredEvents = events.filter { event ->
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
                        "Hockey Events",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALL", "TOURNAMENT", "CAMP", "LEAGUE").forEach { type ->
                        FilterChip(
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

                // Status indicator
                if (filteredEvents.isEmpty()) {
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

                // Events list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredEvents) { event ->
                        ModernEventCard(event = event)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernEventCard(event: Event) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                            text = event.date,
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
                            text = event.type,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // View button (now toggles expanded state)
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

            // Expanded details section

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
