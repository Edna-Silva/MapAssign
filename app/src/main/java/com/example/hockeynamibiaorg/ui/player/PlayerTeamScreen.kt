package com.example.hockeynamibiaorg.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.Team
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.ui.theme.LighterBlue
import com.example.hockeynamibiaorg.ui.theme.DarkBlue
import com.example.hockeynamibiaorg.ui.theme.BlueAccent
import com.example.hockeynamibiaorg.ui.theme.GoldYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTeamScreen(navController: NavController, playerId: String) {
    val db = FirebaseFirestore.getInstance()
    var player by remember { mutableStateOf<Player?>(null) }
    var team by remember { mutableStateOf<Team?>(null) }
    var teammates by remember { mutableStateOf<List<Player>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load player and associated team data
    LaunchedEffect(playerId) {
        try {
            // Load player data
            val playerSnapshot = db.collection("players").document(playerId).get().await()
            player = playerSnapshot.toObject(Player::class.java)

            // If player has a team, fetch team data
            player?.teamId?.let { teamId ->
                if (teamId.isNotBlank()) {
                    val teamSnapshot = db.collection("teams").document(teamId).get().await()
                    team = teamSnapshot.toObject(Team::class.java)

                    // Load teammates (other players on the same team)
                    if (team?.players?.isNotEmpty() == true) {
                        val teammatesList = team!!.players
                            .filter { it != playerId } // Exclude current player
                            .mapNotNull { teammateId ->
                                val snapshot = db.collection("players").document(teammateId).get().await()
                                snapshot.toObject(Player::class.java)
                            }
                        teammates = teammatesList
                    }
                }
            }
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Team", fontWeight = FontWeight.Bold) },
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
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            if (isLoading) {
                // Show loading indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlueAccent)
                }
            } else if (team == null) {
                // Player is not on a team
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = LighterBlue.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "You're not enrolled in any team yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your coach will add you to a team soon",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Player has a team, show team details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Team header with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(DarkBlue, BlueAccent)
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(GoldYellow.copy(alpha = 0.2f))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = GoldYellow,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = team?.name ?: "",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Age Group",
                                    tint = GoldYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = team?.ageGroup ?: "",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Category",
                                    tint = GoldYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = team?.category ?: "",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Player stats section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "My Statistics",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatItem(
                                    icon = Icons.Default.Star,
                                    label = "Goals",
                                    value = player?.goals ?: "0",
                                    iconTint = GoldYellow
                                )

                                StatItem(
                                    icon = Icons.Default.Star,
                                    label = "Points",
                                    value = player?.points ?: "0",
                                    iconTint = GoldYellow
                                )
                            }
                        }
                    }

                    // Teammates section
                    Text(
                        text = "My Teammates",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    if (teammates.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No other teammates on this team yet",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        teammates.forEach { teammate ->
                            TeammateItem(teammate = teammate)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TeammateItem(teammate: Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BlueAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BlueAccent,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Player Information
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "${teammate.firstName} ${teammate.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stats",
                        tint = GoldYellow,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Goals: ${teammate.goals} | Points: ${teammate.points}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            } }
}
}

