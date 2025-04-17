package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
fun TeamRegistrationScreen(navController: NavController) {
    var teamName by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var hockeyType by remember { mutableStateOf("") }

    val viewModel: TeamViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register New Team") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Team Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Age Group Dropdown
            var ageExpanded by remember { mutableStateOf(false) }
            val ageGroups = listOf("U12", "U14", "U16", "U18", "Senior")
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = {},
                    label = { Text("Age Group") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { ageExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Age Group")
                        }
                    }
                )
                DropdownMenu(
                    expanded = ageExpanded,
                    onDismissRequest = { ageExpanded = false }
                ) {
                    ageGroups.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                ageGroup = item
                                ageExpanded = false
                            }
                        )
                    }
                }
            }

            // Gender Dropdown
            var genderExpanded by remember { mutableStateOf(false) }
            val genders = listOf("Male", "Female", "Mixed")
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { genderExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
                        }
                    }
                )
                DropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genders.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                gender = item
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            // Hockey Type Dropdown
            var hockeyExpanded by remember { mutableStateOf(false) }
            val hockeyTypes = listOf("Indoor", "Outdoor")
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = hockeyType,
                    onValueChange = {},
                    label = { Text("Hockey Type") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { hockeyExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Hockey Type")
                        }
                    }
                )
                DropdownMenu(
                    expanded = hockeyExpanded,
                    onDismissRequest = { hockeyExpanded = false }
                ) {
                    hockeyTypes.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                hockeyType = item
                                hockeyExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.registerTeam(
                        name = teamName,
                        ageGroup = ageGroup,
                        gender = gender,
                        hockeyType = hockeyType
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = teamName.isNotBlank() && ageGroup.isNotBlank() &&
                        gender.isNotBlank() && hockeyType.isNotBlank()
            ) {
                Text("Register Team")
            }
        }
    }
}