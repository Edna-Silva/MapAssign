package com.example.hockeynamibiaorg.ui.coach


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hockeynamibiaorg.data.viewModels.CoachProfileViewModel
import com.example.hockeynamibiaorg.ui.theme.DarkBlue

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import coil.compose.rememberAsyncImagePainter
import com.example.hockeynamibiaorg.R
import com.example.hockeynamibiaorg.data.models.Coach
import com.example.hockeynamibiaorg.ui.player.DetailRow
import com.example.hockeynamibiaorg.ui.player.formatDate
import com.example.hockeynamibiaorg.ui.theme.BlueAccent
import com.example.hockeynamibiaorg.ui.theme.GoldYellow
import com.example.hockeynamibiaorg.ui.theme.LighterBlue

@Composable
fun CoachProfileScreen(
    coachId: String,
    navController: NavController,
    viewModel: CoachProfileViewModel = viewModel(),

    ) {
    LaunchedEffect(coachId) {
        viewModel.loadCoachProfile(coachId)
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
        viewModel.coach != null -> {
            CoachProfile(
                coach = viewModel.coach!!,
                teamNames = viewModel.teamNames,
                onCoachUpdate = { viewModel.updateCoach(it) },
                navController = navController
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachProfile(
    coach: Coach,
    teamNames: List<String>,
    onCoachUpdate: (Coach) -> Unit = {},
    navController: NavController
) {
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
                    text = "Hockey Namibia Coach",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Image and Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (coach.profileImageUrl.isNotEmpty()) {
                        rememberAsyncImagePainter(coach.profileImageUrl)
                    } else {
                        painterResource(id = R.drawable.profileimage)
                    },
                    contentDescription = "Coach image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, GoldYellow, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = coach.ageGroup,
                        color = GoldYellow,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${coach.firstName} ${coach.lastName}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    Text(
                        text = "Coach",
                        fontSize = 18.sp,
                        color = BlueAccent
                    )
                }
            }

            // Coach details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showEditDialog = true },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = LighterBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Coach Details",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
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

                    DetailRow("Age Group", coach.ageGroup)
                    DetailRow("Email", coach.email)
                    DetailRow("Phone", coach.phoneNumber)
                    DetailRow("Status", if (coach.isActive) "Active" else "Inactive")
                    DetailRow("Member Since", formatDate(coach.dateJoined))
                }
            }

            // Team Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = LighterBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Teams",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    if (teamNames.isEmpty()) {
                        Text(
                            text = "No teams assigned",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        teamNames.forEach { teamName ->
                            Text(
                                text = teamName,
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = DarkBlue,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // Stats
            if (coach.stats.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = LighterBlue)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Coaching Stats",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue
                        )
                        Text(
                            text = coach.stats,
                            color = DarkBlue,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            // Show dialog
            if (showEditDialog) {
                CoachEditDialog(
                    coach = coach,
                    onDismiss = { showEditDialog = false },
                    onSave = { updatedCoach ->
                        onCoachUpdate(updatedCoach)
                        showEditDialog = false
                    }
                )
            }
        }
    }
}


@Composable
fun CoachEditDialog(
    coach: Coach,
    onDismiss: () -> Unit,
    onSave: (Coach) -> Unit
) {
    var editableCoach by remember { mutableStateOf(coach) }

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
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Edit Coach Details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = DarkBlue
                )

                OutlinedTextField(
                    value = editableCoach.firstName,
                    onValueChange = { editableCoach = editableCoach.copy(firstName = it) },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.lastName,
                    onValueChange = { editableCoach = editableCoach.copy(lastName = it) },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.ageGroup,
                    onValueChange = { editableCoach = editableCoach.copy(ageGroup = it) },
                    label = { Text("Age Group") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.phoneNumber,
                    onValueChange = { editableCoach = editableCoach.copy(phoneNumber = it) },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.email,
                    onValueChange = { editableCoach = editableCoach.copy(email = it) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.stats,
                    onValueChange = { editableCoach = editableCoach.copy(stats = it) },
                    label = { Text("Coaching Stats") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableCoach.teamId,
                    onValueChange = { editableCoach = editableCoach.copy(teamId = it) },
                    label = { Text("Team IDs (comma separated)") },
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
                        onClick = { onSave(editableCoach) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellow)
                    ) {
                        Text("Save", color = DarkBlue)
                    }
                }
            }
        }
    }
}