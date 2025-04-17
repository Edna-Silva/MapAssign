package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.viewModels.TeamViewModel
import com.example.hockeynamibiaorg.data.models.Team
import com.example.hockeynamibiaorg.viewModels.TeamViewModel

@Composable
fun TeamManagementScreen(navController: NavController) {
    val viewModel: TeamViewModel = viewModel()
    val teams by viewModel.teams.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Team Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(teams) { team ->
                TeamCard(
                    team = team,
                    onClick = {
                        navController.navigate("eventManagement/${team.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun TeamCard(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(team.name, style = MaterialTheme.typography.headlineSmall)
            Text("Age Group: ${team.ageGroup}")
            Text("Type: ${team.hockeyType}")
            Text("Players: ${team.playerIds.size}/11")
        }
    }
}