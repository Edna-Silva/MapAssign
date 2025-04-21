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
fun RemovePlayerScreen(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    coachName: String
) {
    var usernameToRemove by remember { mutableStateOf("") }
    var ageGroupToRemove by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val coachTeams = playerViewModel.getTeamsForCurrentCoach()
    val ageGroups = coachTeams.map { it.ageGroup }.distinct()
    var ageGroupMenuExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Remove Player", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

        OutlinedTextField(
            value = usernameToRemove,
            onValueChange = { usernameToRemove = it },
            label = { Text("Player Username") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Column {
            OutlinedTextField(
                value = ageGroupToRemove,
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
                            ageGroupToRemove = group
                            ageGroupMenuExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (usernameToRemove.isNotBlank() && ageGroupToRemove.isNotBlank()) {
                    coroutineScope.launch {
                        playerViewModel.removePlayer(usernameToRemove, ageGroupToRemove)
                        navController.popBackStack()
                        Toast.makeText(context, "Player Removed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter username and age group",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Remove Player")
        }
    }
}