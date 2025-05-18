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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.R
import com.example.hockeynamibiaorg.data.models.Team
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.data.viewModels.TeamViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.ui.theme.LighterBlue
import com.example.hockeynamibiaorg.ui.theme.DarkBlue
import com.example.hockeynamibiaorg.ui.theme.BlueAccent
import com.example.hockeynamibiaorg.ui.theme.GoldYellow




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
                        text = " Hockey Teams",
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
                            painter = painterResource(id = R.drawable.group),
                            contentDescription = "Team Icon",
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
                        gender = "", // TODO
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
                    painter = painterResource(id = R.drawable.group),
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
                        painter = painterResource(id = R.drawable.application),
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
                painter = painterResource(id = R.drawable.group),
                contentDescription = "View details",
                tint = GoldYellow,
                modifier= Modifier.size(20.dp)

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
                                    category,

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
                    //brush =SolidColor/(BlueAccent)
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
                                painter = painterResource(id = R.drawable.application),
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
                        //brush = /*SolidColor/(BlueAccent)*/
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
    var players by remember { mutableStateOf<List<Player>>(emptyList()) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var showEditPlayerDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Load team and players
    LaunchedEffect(teamId) {
        val teamSnapshot = db.collection("teams").document(teamId).get().await()
        team = teamSnapshot.toObject(Team::class.java)

        if (team?.players?.isNotEmpty() == true) {
            val playersList = team!!.players.mapNotNull { playerId ->
                val snapshot = db.collection("players").document(playerId).get().await()
                snapshot.toObject(Player::class.java)
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
                        text = "Team Players",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${players.size} players on roster",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            if (players.isEmpty()) {
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
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = LighterBlue.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No players on this team yet.\nClick the + button to add players.",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(players) { player ->
                        EnhancedPlayerItem(
                            player = player,
                            onClick = {
                                selectedPlayer = player
                                showEditPlayerDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Player Selection Dialog
    if (showAddPlayerDialog) {
        PlayerSelectionDialog(
            onDismiss = { showAddPlayerDialog = false },
            teamId = teamId,
            onPlayerSelected = { newPlayerId ->
                // Add player to team
                val updatedPlayerList = team?.players?.toMutableList() ?: mutableListOf()
                updatedPlayerList.add(newPlayerId)

                db.collection("teams").document(teamId)
                    .update("players", updatedPlayerList)
                    .addOnSuccessListener {
                        // Also update the player's teamId to ensure they're only on one team
                        db.collection("players").document(newPlayerId)
                            .update("teamId", teamId)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Player added to team", Toast.LENGTH_SHORT).show()
                                // Refresh players list
                                db.collection("players").document(newPlayerId).get()
                                    .addOnSuccessListener { snapshot ->
                                        val addedPlayer = snapshot.toObject(Player::class.java)
                                        addedPlayer?.let {
                                            players = players + it
                                        }
                                    }
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                showAddPlayerDialog = false
            }
        )
    }

    // Edit Player Dialog
    if (showEditPlayerDialog && selectedPlayer != null) {
        EditPlayerDialog(
            player = selectedPlayer!!,
            onDismiss = {
                showEditPlayerDialog = false
                selectedPlayer = null
            },
            onSave = { updatedPlayer ->
                db.collection("players").document(updatedPlayer.id)
                    .set(updatedPlayer)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Player updated successfully", Toast.LENGTH_SHORT).show()
                        // Update the local list
                        players = players.map { if (it.id == updatedPlayer.id) updatedPlayer else it }
                        showEditPlayerDialog = false
                        selectedPlayer = null
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            onDeleteRequest = {
                showEditPlayerDialog = false
                showDeleteConfirmation = true
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation && selectedPlayer != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                selectedPlayer = null
            },
            title = { Text("Remove Player", color = DarkBlue) },
            text = {
                Text(
                    "Are you sure you want to remove ${selectedPlayer!!.firstName} from this team?",
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val playerId = selectedPlayer!!.id

                        // Remove player from team's player list
                        val updatedPlayerIds = team?.players?.toMutableList() ?: mutableListOf()
                        updatedPlayerIds.remove(playerId)

                        db.collection("teams").document(teamId)
                            .update("players", updatedPlayerIds)
                            .addOnSuccessListener {
                                // Clear the player's team association
                                db.collection("players").document(playerId)
                                    .update("teamId", "")
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Player removed from team", Toast.LENGTH_SHORT).show()
                                        // Update local list
                                        players = players.filter { it.id != playerId }
                                        showDeleteConfirmation = false
                                        selectedPlayer = null
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteConfirmation = false
                        selectedPlayer = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun EnhancedPlayerItem(player: Player, onClick: () -> Unit) {
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
            // Player Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BlueAccent.copy(alpha = 0.2f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BlueAccent,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Player Information
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "${player.firstName} ${player.lastName}",
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
                        text = player.ageGroup,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stats",
                        tint = GoldYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Goals: ${player.goals} | Points: ${player.points}",
                        color = BlueAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right arrow indicator
            Icon(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "View details",
                tint = GoldYellow,
                modifier= Modifier.size(20.dp)
            )
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
                            painter = painterResource(id = R.drawable.application),
                            contentDescription = "Category",
                            tint = BlueAccent,
                            modifier= Modifier.size(20.dp)

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSelectionDialog(
    onDismiss: () -> Unit,
    teamId: String,
    onPlayerSelected: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var availablePlayers by remember { mutableStateOf<List<Player>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    // Fetch all players who aren't already on a team
    LaunchedEffect(Unit) {
        try {
            val snapshot = db.collection("players")
                .whereEqualTo("teamId", "")  // Players not on any team
                .get()
                .await()

            availablePlayers = snapshot.toObjects(Player::class.java)
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    // Filter players based on search query
    val filteredPlayers = if (searchQuery.isBlank()) {
        availablePlayers
    } else {
        availablePlayers.filter {
            it.firstName.contains(searchQuery, ignoreCase = true) ||
                    it.lastName.contains(searchQuery, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Player", fontWeight = FontWeight.Bold, color = DarkBlue) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Players") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = BlueAccent
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    )
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BlueAccent)
                    }
                } else if (filteredPlayers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No available players found",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(filteredPlayers) { player ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPlayerSelected(player.id) }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(LighterBlue.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = BlueAccent
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "${player.firstName} ${player.lastName}",
                                        fontWeight = FontWeight.SemiBold,
                                        color = DarkBlue
                                    )
                                    Text(
                                        text = player.ageGroup,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BlueAccent
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    // SolidColor(BlueAccent)
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
fun EditPlayerDialog(
    player: Player,
    onDismiss: () -> Unit,
    onSave: (Player) -> Unit,
    onDeleteRequest: () -> Unit
) {
    var firstName by remember { mutableStateOf(player.firstName) }
    var lastName by remember { mutableStateOf(player.lastName) }
    var phoneNumber by remember { mutableStateOf(player.phoneNumber) }
    var email by remember { mutableStateOf(player.email) }
    var ageGroup by remember { mutableStateOf(player.ageGroup) }
    var goals by remember { mutableStateOf(player.goals) }
    var points by remember { mutableStateOf(player.points) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Player", fontWeight = FontWeight.Bold, color = DarkBlue) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "First Name",
                            tint = BlueAccent
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Last Name",
                            tint = BlueAccent
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = BlueAccent
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Number",
                            tint = BlueAccent
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { ageGroup = it },
                    label = { Text("Age Group") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Age Group",
                            tint = BlueAccent
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BlueAccent,
                        focusedLabelColor = BlueAccent,
                        cursorColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Stats section with header
                Text(
                    text = "Player Statistics",
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = goals,
                        onValueChange = { goals = it },
                        label = { Text("Goals") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Goals",
                                tint = GoldYellow
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = BlueAccent,
                            focusedLabelColor = BlueAccent,
                            cursorColor = BlueAccent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = points,
                        onValueChange = { points = it },
                        label = { Text("Points") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Points",
                                tint = GoldYellow
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = BlueAccent,
                            focusedLabelColor = BlueAccent,
                            cursorColor = BlueAccent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val updatedPlayer = player.copy(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            phoneNumber = phoneNumber,
                            ageGroup = ageGroup,
                            goals = goals,
                            points = points
                        )
                        onSave(updatedPlayer)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Changes")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDeleteRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Remove From Team")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BlueAccent
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        // SolidColor(BlueAccent)
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