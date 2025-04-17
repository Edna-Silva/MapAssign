package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.viewModels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsScreen(navController: NavController, playerId: String) {
    val viewModel: TeamViewModel = viewModel()
    val player by viewModel.getPlayer(playerId).collectAsState(null)
    var showTeamDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            player?.let {
                Text("Username: ${it.username}", style = MaterialTheme.typography.bodyLarge)
                Text("Age Group: ${it.ageGroup}", style = MaterialTheme.typography.bodyLarge)
                Text("Current Team: ${it.teamId}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showTeamDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Team")
                }
            }
        }

        if (showTeamDialog) {
            TeamSelectionDialog(
                onDismiss = { showTeamDialog = false },
                onTeamSelected = { newTeamId ->
                    player?.let {
                        viewModel.updatePlayerTeam(it.id, newTeamId)
                        showTeamDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun TeamSelectionDialog(onDismiss: () -> Unit, onTeamSelected: (String) -> Unit) {
    val viewModel: TeamViewModel = viewModel()
    val teams by viewModel.teams.collectAsState(emptyList())
    var selectedTeam by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select New Team") },
        text = {
            Column {
                teams.forEach { team ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTeam = team.id
                            }
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = selectedTeam == team.id,
                            onClick = { selectedTeam = team.id }
                        )
                        Text(team.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedTeam.isNotBlank()) {
                        onTeamSelected(selectedTeam)
                    }
                },
                enabled = selectedTeam.isNotBlank()
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}