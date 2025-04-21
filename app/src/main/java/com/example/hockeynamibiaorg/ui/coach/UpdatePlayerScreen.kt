package com.example.myapplication

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
fun UpdatePlayerScreen(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    coachName: String
) {
    var usernameToUpdate by remember { mutableStateOf("") }
    var ageGroupToUpdate by remember { mutableStateOf("") }
    var newTeamName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val coachTeams = playerViewModel.getTeamsForCurrentCoach()
    val ageGroups = coachTeams.map { it.ageGroup }.distinct()
    var ageGroupMenuExpanded by remember { mutableStateOf(false) }
    var teamMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Update Player", style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold))

        OutlinedTextField(
            value = usernameToUpdate,
            onValueChange = { usernameToUpdate = it },
            label = { Text("Player Username") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Text("Age Group", style = TextStyle(fontWeight = FontWeight.Bold))
        Column {
            OutlinedTextField(
                value = ageGroupToUpdate,
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
                            ageGroupToUpdate = group
                            ageGroupMenuExpanded = false
                        }
                    )
                }
            }
        }

        Text("New Team Name", style = TextStyle(fontWeight = FontWeight.Bold))
        Column {
            OutlinedTextField(
                value = newTeamName,
                onValueChange = { teamMenuExpanded = true },
                label = { Text("New Team Name") },
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
                            newTeamName = team.name
                            teamMenuExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (usernameToUpdate.isNotBlank() && ageGroupToUpdate.isNotBlank() && newTeamName.isNotBlank()) {
                    coroutineScope.launch {
                        playerViewModel.updatePlayer(usernameToUpdate, ageGroupToUpdate, newTeamName)
                        navController.popBackStack()
                        Toast.makeText(context, "Player Updated", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Update Player")
        }
    }
}