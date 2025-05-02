package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.Team
import com.example.hockeynamibiaorg.ui.theme.Purple80
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""

    var teams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var showAddTeamDialog by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var showTeamDialog by remember { mutableStateOf(false) }

    // Fetch teams for the current coach
    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            db.collection("teams")
                .whereEqualTo("coachId", currentUserId)
                .get()
                .addOnSuccessListener { result ->
                    teams = result.toObjects(Team::class.java)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Team Management") },
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
            FloatingActionButton(
                onClick = { showAddTeamDialog = true },
                containerColor = Purple80
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Team")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp))
                {
                    Text(
                        text = "Your Hockey Teams",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (teams.isEmpty()) {
                        Text(
                            text = "No teams yet. Click the + button to add a team.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(teams) { team ->
                                TeamCard(
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

            // Add Team Dialog
            if (showAddTeamDialog) {
                AddTeamDialog(
                    onDismiss = { showAddTeamDialog = false },
                    onSave = { teamName, ageGroup, category ->
                        val newTeam = Team(
                            id = db.collection("teams").document().id,
                            name = teamName,
                            ageGroup = ageGroup,
                            category = category,
                            coachId = currentUserId
                        )
                        db.collection("teams").document(newTeam.id).set(newTeam)
                        showAddTeamDialog = false
                    }
                )
            }

            // Team Actions Dialog
            if (showTeamDialog && selectedTeam != null) {
                TeamActionsDialog(
                    team = selectedTeam!!,
                    onDismiss = {
                        showTeamDialog = false
                        selectedTeam = null
                    },
                    onDelete = {
                        db.collection("teams").document(selectedTeam!!.id).delete()
                        showTeamDialog = false
                        selectedTeam = null
                    },
                    onEdit = {
                        // This would navigate to an edit screen
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
fun TeamCard(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E88E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White
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
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${team.ageGroup} • ${team.category}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "${team.players.size} players",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AddTeamDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var teamName by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Team") },
        text = {
            Column {
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { ageGroup = it },
                    label = { Text("Age Group (e.g., U12, U14, U16)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (Indoor/Outdoor/Mixed)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (teamName.isNotBlank() && ageGroup.isNotBlank() && category.isNotBlank()) {
                        onSave(teamName, ageGroup, category)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamActionsDialog(
    team: Team,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onViewPlayers: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(team.name) },
        text = {
            Column {
                Text("Age Group: ${team.ageGroup}")
                Text("Category: ${team.category}")
                Text("Players: ${team.players.size}")
            }
        },
        buttons = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onViewPlayers,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Players")
                }
                Button(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Team")
                }
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Team")
                }
            }
        },
        modifier = TODO(),
        properties = TODO(),
        content = TODO()
    )
}@Composable
fun TeamPlayersScreen(navController: NavController, teamId: String) {
    val db = FirebaseFirestore.getInstance()
    var team by remember { mutableStateOf<Team?>(null) }
    var players by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(teamId) {
        // Fetch team
        val teamSnapshot = db.collection("teams").document(teamId).get().await()
        team = teamSnapshot.toObject(Team::class.java)

        // Fetch players
        if (team?.players?.isNotEmpty() == true) {
            val playersList = mutableListOf<User>()
            for (playerId in team!!.players) {
                val playerSnapshot = db.collection("users").document(playerId).get().await()
                playerSnapshot.toObject(User::class.java)?.let { playersList.add(it) }
            }
            players = playersList
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team?.name ?: "Team Players") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(players) { player ->
                PlayerItem(player = player)
            }
        }
    }
}

@Composable
fun PlayerItem(player: User) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = player.name, fontWeight = FontWeight.Bold)
                Text(text = player.email)
            }
        }
    }
}
@Composable
fun EditTeamScreen(navController: NavController, teamId: String) {
    val db = FirebaseFirestore.getInstance()
    var team by remember { mutableStateOf<Team?>(null) }

    var teamName by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

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
                title = { Text("Edit Team") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        team?.let { currentTeam ->
                            val updatedTeam = currentTeam.copy(
                                name = teamName,
                                ageGroup = ageGroup,
                                category = category
                            )
                            db.collection("teams").document(teamId).set(updatedTeam)
                            navController.navigateUp()
                        }
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Team Name") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            OutlinedTextField(
                value = ageGroup,
                onValueChange = { ageGroup = it },
                label = { Text("Age Group") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}
