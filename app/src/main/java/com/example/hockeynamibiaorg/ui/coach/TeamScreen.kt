package com.example.hockeynamibiaorg.ui.coach

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.Team
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.data.viewModels.TeamViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Define color palette to match PlayerHomeScreen
val DarkBlue = Color(0xFF0D3B66)
val BlueAccent = Color(0xFF2A6EBB)
val LighterBlue = Color(0xFF4A90E2)
val GoldYellow = Color(0xFFFFD166)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(navController: NavController) {
    val teamViewModel: TeamViewModel = viewModel()
    val context = LocalContext.current
    val teams by teamViewModel.teams.collectAsState()

    var showAddTeamDialog by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var showTeamDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        teamViewModel.loadTeams()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Team Management",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTeamDialog = true },
                containerColor = GoldYellow,
                contentColor = DarkBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Team")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Header Section with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF142143), Color(0xFF3F5291))
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Your Hockey Teams",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Manage teams and players",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Teams List Section
            if (teams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = LighterBlue.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No teams yet. Click the + button to add a team.",
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(teams) { team ->
                        EnhancedTeamCard(
                            team = team,
                            onClick = {
                                selectedTeam = team
                                showTeamDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showAddTeamDialog) {
            EnhancedAddTeamDialog(
                onDismiss = { showAddTeamDialog = false },
                onSave = { teamName, ageGroup, category ->
                    teamViewModel.registerTeam(
                        name = teamName,
                        ageGroup = ageGroup,
                        gender = "", // Add gender later
                        category = category,
                        onSuccess = {
                            Toast.makeText(context, "Team created successfully", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }

        if (showTeamDialog && selectedTeam != null) {
            EnhancedTeamActionsDialog(
                team = selectedTeam!!,
                onDismiss = {
                    showTeamDialog = false
                    selectedTeam = null
                },
                onDelete = {
                    teamViewModel.deleteTeam(selectedTeam!!.id)
                    showTeamDialog = false
                    selectedTeam = null
                },
                onEdit = {
                    navController.navigate("editTeam/${selectedTeam?.id}")
                    showTeamDialog = false
                },
                onViewPlayers = {
                    navController.navigate("teamPlayers/${selectedTeam?.id}")
                    showTeamDialog = false
                }
            )
        }
    }
}

@Composable
fun EnhancedTeamCard(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Team Icon with hockey theme
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BlueAccent.copy(alpha = 0.2f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = BlueAccent,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Team Information
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = team.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Age Group",
                        tint = BlueAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = team.ageGroup,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Category",
                        tint = BlueAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = team.category,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Players",
                        tint = BlueAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${team.players.size} players",
                        color = BlueAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Arrow icon
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "View details",
                tint = GoldYellow
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedAddTeamDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    val teamViewModel: TeamViewModel = viewModel()
    val context = LocalContext.current

    var teamName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                "Add New Team",
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Team",
                            tint = BlueAccent
                        )
                    }
                )

                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { ageGroup = it },
                    label = { Text("Age Group (e.g., U12, U14, U16)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Age Group",
                            tint = BlueAccent
                        )
                    }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (Indoor/Outdoor/Mixed)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Category",
                            tint = BlueAccent
                        )
                    }
                )

                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender (Female/Male)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Gender",
                            tint = BlueAccent
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (teamName.isNotBlank() && ageGroup.isNotBlank() && category.isNotBlank()) {
                        teamViewModel.registerTeam(
                            name = teamName,
                            ageGroup = ageGroup,
                            gender = gender,
                            category = category,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Team registered successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onSave(
                                    teamName,
                                    ageGroup,
                                    category
                                )
                                onDismiss()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueAccent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Team")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BlueAccent
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    /*brush = /*SolidColor*/(BlueAccent)*/
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTeamActionsDialog(
    team: Team,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onViewPlayers: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                team.name,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LighterBlue.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Age Group",
                                tint = BlueAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Age Group: ${team.ageGroup}",
                                fontSize = 16.sp,
                                color = DarkBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Category",
                                tint = BlueAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Category: ${team.category}",
                                fontSize = 16.sp,
                                color = DarkBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Players",
                                tint = BlueAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Players: ${team.players.size}",
                                fontSize = 16.sp,
                                color = DarkBlue
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onViewPlayers,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Players")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldYellow,
                        contentColor = DarkBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Team")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Team")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BlueAccent
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        /*brush = /*SolidColor*/(BlueAccent)*/
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamPlayersScreen(navController: NavController, teamId: String) {
    val db = FirebaseFirestore.getInstance()
    var team by remember { mutableStateOf<Team?>(null) }
    var players by remember { mutableStateOf<List<User>>(emptyList()) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<User?>(null) }

    // Load team and players
    LaunchedEffect(teamId) {
        val teamSnapshot = db.collection("teams").document(teamId).get().await()
        team = teamSnapshot.toObject(Team::class.java)

        if (team?.players?.isNotEmpty() == true) {
            val playersList = team!!.players.mapNotNull { playerId ->
                val snapshot = db.collection("users").document(playerId).get().await()
                snapshot.toObject(User::class.java)
            }
            players = playersList
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team?.name ?: "Team Players", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPlayerDialog = true },
                containerColor = GoldYellow,
                contentColor = DarkBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Player")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            if (players.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No players found. Tap + to add a player.")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(players) { player ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedPlayer = player },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = LighterBlue.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = BlueAccent)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(player.firstName ?: "Unnamed Player", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        if (showAddPlayerDialog) {
            AddPlayerDialog(
                onDismiss = { showAddPlayerDialog = false },
                onAdd = { newPlayerId ->
                    // Update team with new player ID
                    val updatedPlayerList = team?.players?.toMutableList() ?: mutableListOf()
                    updatedPlayerList.add(newPlayerId)
                    db.collection("teams").document(teamId)
                        .update("players", updatedPlayerList)
                        .addOnSuccessListener {
                            showAddPlayerDialog = false
                        }
                }
            )
        }

        if (selectedPlayer != null) {
            EditDeletePlayerDialog(
                player = selectedPlayer!!,
                onDismiss = { selectedPlayer = null },
                onEdit = { /* Navigate to edit screen or implement edit logic */ },
                onDelete = {
                    // Remove player from team
                    val updated = team?.players?.toMutableList()
                    updated?.remove(selectedPlayer!!.id)
                    db.collection("teams").document(teamId)
                        .update("players", updated)
                        .addOnSuccessListener {
                            players = players.filter { it.id != selectedPlayer!!.id }
                            selectedPlayer = null
                        }
                }
            )
        }
    }
}


@Composable
fun EnhancedPlayerItem(player: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(LighterBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BlueAccent,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = player.firstName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = player.email,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTeamScreen(navController: NavController, teamId: String) {
    val db = FirebaseFirestore.getInstance()
    var team by remember { mutableStateOf<Team?>(null) }
    var teamName by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(teamId) {
        val teamSnapshot = db.collection("teams").document(teamId).get().await()
        team = teamSnapshot.toObject(Team::class.java)
        team?.let {
            teamName = it.name
            ageGroup = it.ageGroup
            category = it.category
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Team",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    team?.let { currentTeam ->
                        if (teamName.isBlank() || ageGroup.isBlank() || category.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@FloatingActionButton
                        }

                        val updatedTeam = currentTeam.copy(
                            name = teamName,
                            ageGroup = ageGroup,
                            category = category
                        )
                        db.collection("teams").document(teamId).set(updatedTeam)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Team updated successfully", Toast.LENGTH_SHORT).show()
                                navController.navigateUp()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                containerColor = GoldYellow,
                contentColor = DarkBlue
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Header section with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkBlue, BlueAccent)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Edit Team Details",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Update your team information",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Form section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Team",
                            tint = BlueAccent
                        )
                    }
                )

                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { ageGroup = it },
                    label = { Text("Age Group (e.g., U12, U14, U16)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Age Group",
                            tint = BlueAccent
                        )
                    }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (Indoor/Outdoor/Mixed)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Category",
                            tint = BlueAccent
                        )
                    }
                )
            }
        }
    }
}
@Composable
fun AddPlayerDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var playerName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Player") },
        text = {
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Player Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                val newPlayerId = FirebaseFirestore.getInstance().collection("users").document().id
                val newUser = User(id = newPlayerId, firstName = playerName)
                FirebaseFirestore.getInstance().collection("users").document(newPlayerId)
                    .set(newUser)
                    .addOnSuccessListener {
                        onAdd(newPlayerId)
                    }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
@Composable
fun EditDeletePlayerDialog(
    player: User,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(player.firstName ?: "Player") },
        text = { Text("What would you like to do with this player?") },
        confirmButton = {
            Column {
                Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                    Text("Edit Player")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Player")
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    )
}
