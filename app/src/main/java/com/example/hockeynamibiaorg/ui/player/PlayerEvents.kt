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
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Define colors from the palette
val DarkBlue = Color(0xFF142143)
val GoldYellow = Color(0xFFFFAF00)
val LightGray = Color(0xFFE4E4E4)
val BlueAccent = Color(0xFF1A5D94)
val LighterBlue = Color(0xFF2A6DA4)

enum class EventType {
    ALL, TOURNAMENT, CAMP, LEAGUE
}

data class Event(
    val name: String,
    val date: String,
    val location: String,
    val type: EventType,
    val description: String
)

fun getSampleEvents(): List<Event> {
    return listOf(
        Event(
            "National Tournament",
            "May 15, 2025",
            "Windhoek National Stadium",
            EventType.TOURNAMENT,
            "The premier hockey tournament in Namibia featuring top teams from across the country. Join us for exciting matches and great competition."
        ),
        Event(
            "Regional Championship",
            "June 23, 2025",
            "Swakopmund Sports Center",
            EventType.TOURNAMENT,
            "Teams from the coastal region compete for the regional title. This event showcases local talent and promotes hockey in the region."
        ),
        Event(
            "Youth Hockey Camp",
            "July 10, 2025",
            "Windhoek Hockey Club",
            EventType.CAMP,
            "A week-long camp for young players aged 8-16. Professional coaches will teach skills, strategy, and teamwork in a fun environment."
        ),
        Event(
            "League Season Opening",
            "August 5, 2025",
            "Various Locations",
            EventType.LEAGUE,
            "The official start of the 2025 hockey league season. All divisions begin their competition with matches scheduled across the country."
        ),
        Event(
            "Junior Development Camp",
            "September 12, 2025",
            "Oshakati Sports Complex",
            EventType.CAMP,
            "A specialized camp focusing on developing junior hockey talents. Professional coaches will be present to scout promising players."
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEntriesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(EventType.ALL) }
    var filteredEvents by remember { mutableStateOf(getSampleEvents()) }

    // Update filtered events when filter changes or search query changes
    LaunchedEffect(selectedFilter, searchQuery) {
        filteredEvents = getSampleEvents().filter { event ->
            // Apply type filter
            val matchesType = selectedFilter == EventType.ALL || event.type == selectedFilter

            // Apply search filter if there's a query
            val matchesSearch = if (searchQuery.isNotEmpty()) {
                event.name.contains(searchQuery, ignoreCase = true) ||
                        event.location.contains(searchQuery, ignoreCase = true)
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
                    titleContentColor = DarkBlue,
                    navigationIconContentColor = DarkBlue
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
                        focusedBorderColor = BlueAccent,
                        unfocusedBorderColor = LightGray,
                        cursorColor = BlueAccent
                    ),
                    singleLine = true
                )

                // Header with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(DarkBlue, BlueAccent)
                            )
                        )
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ) {
                    Column {
                        Text(
                            text = "Upcoming Events",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Discover hockey events in Namibia",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }

                // Filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == EventType.ALL,
                        onClick = { selectedFilter = EventType.ALL },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldYellow,
                            selectedLabelColor = DarkBlue
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == EventType.TOURNAMENT,
                        onClick = { selectedFilter = EventType.TOURNAMENT },
                        label = { Text("Tournaments") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldYellow,
                            selectedLabelColor = DarkBlue,
                            containerColor = LightGray,
                            labelColor = DarkBlue
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == EventType.CAMP,
                        onClick = { selectedFilter = EventType.CAMP },
                        label = { Text("Camps") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldYellow,
                            selectedLabelColor = DarkBlue,
                            containerColor = LightGray,
                            labelColor = DarkBlue
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == EventType.LEAGUE,
                        onClick = { selectedFilter = EventType.LEAGUE },
                        label = { Text("League") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldYellow,
                            selectedLabelColor = DarkBlue,
                            containerColor = LightGray,
                            labelColor = DarkBlue
                        )
                    )
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
                                colors = listOf(GoldYellow, GoldYellow.copy(alpha = 0.7f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = event.date.split(" ")[1].replace(",", ""),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DarkBlue
                        )
                        Text(
                            text = event.date.split(" ")[0],
                            fontSize = 14.sp,
                            color = DarkBlue.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,  // Changed from Home to DateRange
                            contentDescription = null,
                            tint = BlueAccent,
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
                            tint = BlueAccent,
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
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(start = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BlueAccent
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
                    Divider(color = LightGray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = BlueAccent,
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
                            EventType.TOURNAMENT -> LighterBlue.copy(alpha = 0.1f)
                            EventType.CAMP -> GoldYellow.copy(alpha = 0.1f)
                            EventType.LEAGUE -> DarkBlue.copy(alpha = 0.1f)
                            else -> LightGray
                        }
                    ) {
                        Text(
                            text = "Type: ${event.type.name.lowercase().capitalize()}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = when(event.type) {
                                EventType.TOURNAMENT -> LighterBlue
                                EventType.CAMP -> DarkBlue
                                EventType.LEAGUE -> DarkBlue
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

@Preview(showBackground = true)
@Composable
fun FunctionalEventsPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        EventEntriesScreen(navController)
    }
}