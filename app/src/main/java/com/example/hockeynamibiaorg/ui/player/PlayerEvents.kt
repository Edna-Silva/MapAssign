package com.example.hockeynamibiaorg.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.ui.theme.Purple80
import com.example.hockeynamibiaorg.ui.theme.Purple80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEntriesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Entries") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple80,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(getSampleEvents()) { event ->
                    EventCard(event = event)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

data class Event(
    val name: String,
    val date: String,
    val location: String,
    val registrationOpen: Boolean
)

fun getSampleEvents(): List<Event> {
    return listOf(
        Event("National Tournament", "May 15, 2025", "Windhoek National Stadium", true),
        Event("Regional Championship", "June 23, 2025", "Swakopmund Sports Center", true),
        Event("Youth Hockey Camp", "July 10, 2025", "Windhoek Hockey Club", false),
        Event("League Season Opening", "August 5, 2025", "Various Locations", true)
    )
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Date: ${event.date}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Location: ${event.location}")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (event.registrationOpen) "Registration: Open" else "Registration: Closed",
                color = if (event.registrationOpen) Color.Green else Color.Red
            )

            if (event.registrationOpen) {
                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = { /* Registration logic */ },
                    modifier = Modifier.align(androidx.compose.ui.Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Register",
                        tint = Purple80
                    )
                }
            }
        }
    }
}


