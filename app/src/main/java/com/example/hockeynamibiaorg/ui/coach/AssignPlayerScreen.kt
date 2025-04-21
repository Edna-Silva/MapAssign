package com.example.myapplication

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun AssignPlayerScreen(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    coachName: String,
    ageGroup: String
) {
    var username by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("") }
    var selectedAgeGroup by remember { mutableStateOf(ageGroup) }
    var selectedTeam by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val coachTeams = playerViewModel.getTeamsForCurrentCoach()
    val ageGroups = listOf("U10", "U12", "U14")
    var ageGroupMenuExpanded by remember { mutableStateOf(false) }
    var teamMenuExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Assign Player", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Player Username") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Player Name") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Column {
            OutlinedTextField(
                value = selectedAgeGroup,
                onValueChange = { ageGroupMenuExpanded = true },
                label = { Text("Age Group") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                trailingIcon = {
                    IconButton(onClick = { ageGroupMenuExpanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Open Dropdown")
                    }
                }
            )
            DropdownMenu(
                expanded = ageGroupMenuExpanded,
                onDismissRequest = { ageGroupMenuExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                ageGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group) },
                        onClick = {
                            selectedAgeGroup = group
                            ageGroupMenuExpanded = false
                        }
                    )
                }
            }

        }

        Column {
            OutlinedTextField(
                value = selectedTeam,
                onValueChange = { teamMenuExpanded = true },
                label = { Text("Team Name") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                trailingIcon = {
                    IconButton(onClick = { teamMenuExpanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Open Dropdown")
                    }
                }
            )
            DropdownMenu(
                expanded = teamMenuExpanded,
                onDismissRequest = { teamMenuExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                coachTeams.forEach { team ->
                    DropdownMenuItem(
                        text = { Text(team.name) },
                        onClick = {
                            selectedTeam = team.name
                            teamMenuExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (username.isNotBlank() && playerName.isNotBlank() && selectedTeam.isNotBlank() && selectedAgeGroup.isNotBlank()) {
                    val newPlayer = Player(username, playerName, selectedAgeGroup, selectedTeam)
                    coroutineScope.launch {
                        playerViewModel.addPlayer(newPlayer)
                        navController.popBackStack()
                        Toast.makeText(context, "Player Added", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()

                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Add Player")
        }
    }
}

