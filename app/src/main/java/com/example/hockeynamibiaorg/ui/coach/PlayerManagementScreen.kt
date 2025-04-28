package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerManagementScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }

    val tabTitles = listOf("My Team Players", "All Players")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Management") },
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
        },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { showAddPlayerDialog = true },
                    containerColor = Purple80,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Player")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Purple80,
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> TeamPlayersList(navController)
                1 -> AllPlayersList(navController)
            }
        }

        // Add Player Dialog
        if (showAddPlayerDialog) {
            AddPlayerDialog(
                onDismiss = { showAddPlayerDialog = false },
                onConfirm = { playerName, ageGroup, teamName ->
                    // Handle adding the new player
                    showAddPlayerDialog = false
                    // You would typically call a viewModel function here to add the player
                }
            )
        }
    }
}

@Composable
fun TeamPlayersList(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(getSampleTeamPlayers()) { player ->
            var showUpdateDialog by remember { mutableStateOf(false) }
            var showDeleteConfirmation by remember { mutableStateOf(false) }

            PlayerCard(
                player = player,
                onEditClick = { showUpdateDialog = true },
                onDeleteClick = { showDeleteConfirmation = true }
            )

            // Update Player Dialog
            if (showUpdateDialog) {
                UpdatePlayerDialog(
                    currentPlayer = player,
                    onDismiss = { showUpdateDialog = false },
                    onConfirm = { ageGroup, teamMember ->
                        // Handle updating the player
                        showUpdateDialog = false
                        // You would typically call a viewModel function here to update the player
                    }
                )
            }

            // Delete Confirmation Dialog
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Delete Player") },
                    text = { Text("Are you sure you want to remove ${player.name} from your team?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteConfirmation = false
                                navController.navigate("removePlayer/${player.id}")
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var teamName by remember { mutableStateOf("") }
    var ageGroupExpanded by remember { mutableStateOf(false) }
    val ageGroups = listOf("Under 12", "Under 14", "Under 16", "Under 18", "Senior")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Player") },
        text = {
            Column {
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Player Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Age Group Dropdown
                ExposedDropdownMenuBox(
                    expanded = ageGroupExpanded,
                    onExpandedChange = { ageGroupExpanded = it }
                ) {
                    OutlinedTextField(
                        value = ageGroup,
                        onValueChange = {},
                        label = { Text("Age Group") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = ageGroupExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = ageGroupExpanded,
                        onDismissRequest = { ageGroupExpanded = false }
                    ) {
                        ageGroups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    ageGroup = group
                                    ageGroupExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(playerName, ageGroup, teamName) },
                enabled = playerName.isNotBlank() && ageGroup.isNotBlank() && teamName.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePlayerDialog(
    currentPlayer: Player,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var ageGroup by remember { mutableStateOf("") }
    var teamMember by remember { mutableStateOf(currentPlayer.name) }
    var ageGroupExpanded by remember { mutableStateOf(false) }
    val ageGroups = listOf("Under 12", "Under 14", "Under 16", "Under 18", "Senior")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Player") },
        text = {
            Column {
                // Age Group Dropdown
                ExposedDropdownMenuBox(
                    expanded = ageGroupExpanded,
                    onExpandedChange = { ageGroupExpanded = it }
                ) {
                    OutlinedTextField(
                        value = ageGroup,
                        onValueChange = {},
                        label = { Text("Age Group") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = ageGroupExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = ageGroupExpanded,
                        onDismissRequest = { ageGroupExpanded = false }
                    ) {
                        ageGroups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    ageGroup = group
                                    ageGroupExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = teamMember,
                    onValueChange = { teamMember = it },
                    label = { Text("Team Member") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(ageGroup, teamMember) },
                enabled = ageGroup.isNotBlank() && teamMember.isNotBlank()
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AllPlayersList(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(getSampleAllPlayers()) { player ->
            PlayerCard(
                player = player,
                onEditClick = { /* Disabled for all players */ },
                onDeleteClick = { /* Disabled for all players */ }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PlayerCard(
    player: Player,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Purple80.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Player",
                    tint = Purple80
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = player.name,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${player.position} • ${player.teamName}",
                    color = Color.Gray
                )

                Text(
                    text = "#${player.jerseyNumber}",
                    color = Purple80,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Purple80
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

// Keep the data class and sample data functions as they were
data class Player(
    val id: Int,
    val name: String,
    val position: String,
    val teamName: String,
    val jerseyNumber: String
)

fun getSampleTeamPlayers(): List<Player> {
    return listOf(
        Player(1, "John Doe", "Forward", "Windhoek Warriors", "10"),
        Player(2, "Sarah Smith", "Defender", "Windhoek Warriors", "7"),
        Player(3, "Michael Johnson", "Goalkeeper", "Windhoek Warriors", "1"),
        Player(4, "Emily Brown", "Forward", "Windhoek Warriors", "9")
    )
}

fun getSampleAllPlayers(): List<Player> {
    return listOf(
        Player(1, "John Doe", "Forward", "Windhoek Warriors", "10"),
        Player(2, "Sarah Smith", "Defender", "Windhoek Warriors", "7"),
        Player(3, "Michael Johnson", "Goalkeeper", "Windhoek Warriors", "1"),
        Player(4, "Emily Brown", "Forward", "Windhoek Warriors", "9"),
        Player(5, "David Miller", "Defender", "Swakopmund Sharks", "5"),
        Player(6, "Jennifer Wilson", "Forward", "Coastal Strikers", "11"),
        Player(7, "Robert Taylor", "Midfielder", "Namib Lions", "8"),
        Player(8, "Amanda Garcia", "Goalkeeper", "Desert Eagles", "1")
    )
}