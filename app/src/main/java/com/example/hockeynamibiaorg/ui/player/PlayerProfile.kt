package com.example.hockeynamibiaorg.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hockeynamibiaorg.data.viewModels.PlayerProfileViewModel
import com.example.hockeynamibiaorg.ui.theme.DarkBlue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions


import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.ui.theme.GoldYellow
import com.example.hockeynamibiaorg.ui.theme.LighterBlue


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter
import com.example.hockeynamibiaorg.R
import com.example.hockeynamibiaorg.ui.theme.BlueAccent


@Composable
fun PlayerProfileScreen(
    playerId: String,
    navController: NavController,
    viewModel: PlayerProfileViewModel = viewModel()
) {
    LaunchedEffect(playerId) {
        viewModel.loadPlayerProfile(playerId)
    }

    when {
        viewModel.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }
        viewModel.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.error ?: "Unknown error",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }
        viewModel.player != null -> {
            PlayerProfile(
                player = viewModel.player!!,
                onPlayerUpdate = { viewModel.updatePlayer(it) },
                navController=navController
            )
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No profile data available",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfile(player: Player, onPlayerUpdate: (Player) -> Unit = {}, navController:NavController) {
    var showEditDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Coach Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF142143),
                    navigationIconContentColor = Color(0xFF142143)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(DarkBlue)
            ) {
                Text(
                    text = "Hockey Namibia Player",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        // Player image and basic info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (player.profileImageUrl.isNotEmpty()) {
                    rememberAsyncImagePainter(player.profileImageUrl)
                } else {
                    painterResource(id = R.drawable.profileimage)
                },
                contentDescription = "Player image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, GoldYellow, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = player.ageGroup,
                    color = GoldYellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${player.firstName} ${player.lastName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                Text(
                    text = "Player",
                    fontSize = 18.sp,
                    color = BlueAccent
                )
            }
        }

        // Player details card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { showEditDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LighterBlue)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Player Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = DarkBlue
                    )
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Details",
                            tint = DarkBlue
                        )
                    }
                }

                DetailRow("Age Group", player.ageGroup)
                DetailRow("Email", player.email)
                DetailRow("Phone", player.phoneNumber)
                DetailRow("Status", if (player.isActive) "Active" else "Inactive")
                DetailRow("Member Since", formatDate(player.dateJoined))
            }
        }

        // Stats card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LighterBlue)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Season Stats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = DarkBlue
                )

                StatRow("Goals", player.goals)
                StatRow("Points", player.points)
                StatRow("Additional Stats", player.stats)
            }
        }
    }

    // Show edit dialog if needed
    if (showEditDialog) {
        PlayerEditDialog(
            player = player,
            onDismiss = { showEditDialog = false },
            onSave = { updatedPlayer ->
                onPlayerUpdate(updatedPlayer)
                showEditDialog = false
            }
        )
    }
    }
}

@Composable
fun PlayerEditDialog(
    player: Player,
    onDismiss: () -> Unit,
    onSave: (Player) -> Unit
) {
    var editablePlayer by remember { mutableStateOf(player) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LighterBlue)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                Text(
                    text = "Edit Player Details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = DarkBlue
                )

                OutlinedTextField(
                    value = editablePlayer.firstName,
                    onValueChange = { editablePlayer = editablePlayer.copy(firstName = it) },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.lastName,
                    onValueChange = { editablePlayer = editablePlayer.copy(lastName = it) },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.ageGroup,
                    onValueChange = { editablePlayer = editablePlayer.copy(ageGroup = it) },
                    label = { Text("Age Group") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.phoneNumber,
                    onValueChange = { editablePlayer = editablePlayer.copy(phoneNumber = it) },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.email,
                    onValueChange = { editablePlayer = editablePlayer.copy(email = it) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.goals,
                    onValueChange = { editablePlayer = editablePlayer.copy(goals = it) },
                    label = { Text("Goals") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.points,
                    onValueChange = { editablePlayer = editablePlayer.copy(points = it) },
                    label = { Text("Points") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePlayer.stats,
                    onValueChange = { editablePlayer = editablePlayer.copy(stats = it) },
                    label = { Text("Additional Stats") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = LighterBlue)
                    ) {
                        Text("Cancel", color = DarkBlue)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onSave(editablePlayer) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellow)
                    ) {
                        Text("Save", color = DarkBlue)
                    }
                }
            }
        }
    }
}
@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, color = DarkBlue)
        Text(text = value, color = DarkBlue)
    }
}

@Composable
fun StatRow(statName: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = DarkBlue
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            color = DarkBlue
        )
    }
}

// Helper function to format date
fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return format.format(date)
}