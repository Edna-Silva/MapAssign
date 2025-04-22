package com.example.hockeynamibiaorg.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hockeynamibiaorg.R

data class HockeyPlayer(
    val name: String,
    val number: Int,
    val position: String,
    val team: String,
    val age: Int,
    val height: String,
    val weight: String,
    val stats: PlayerStats,
    val imageRes: Int
)

data class PlayerStats(
    val goals: Int,
    val assists: Int,
    val points: Int,
    val plusMinus: Int,
    val penaltyMinutes: Int
)

@Composable
fun PlayerProfile(player: HockeyPlayer, onPlayerUpdate: (HockeyPlayer) -> Unit = {}) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editablePlayer by remember { mutableStateOf(player.copy()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header with team colors
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF003366))
        ) {
            Text(
                text = player.team,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center))

        }

        // Player image and basic info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = player.imageRes),
                contentDescription = "Player image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFFCC0000), CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "#${player.number}",
                    color = Color(0xFFCC0000),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = player.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = player.position,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }

        // Player details card - now clickable with edit icon
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { showEditDialog = true },
            shape = RoundedCornerShape(8.dp)
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
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Details",
                            tint = Color(0xFF003366)
                        )
                    }
                }

                DetailRow("Age", player.age.toString())
                DetailRow("Height", player.height)
                DetailRow("Weight", player.weight)
            }
        }

        // Stats card (unchanged from original)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Season Stats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                StatRow("Goals", player.stats.goals.toString())
                StatRow("Assists", player.stats.assists.toString())
                StatRow("Points", player.stats.points.toString())
                StatRow("+/-", player.stats.plusMinus.toString())
                StatRow("PIM", player.stats.penaltyMinutes.toString())
            }
        }
    }

    // Edit Dialog
    if (showEditDialog) {
        Dialog(onDismissRequest = { showEditDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Edit Player Details",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = editablePlayer.name,
                        onValueChange = { editablePlayer = editablePlayer.copy(name = it) },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editablePlayer.number.toString(),
                        onValueChange = { editablePlayer = editablePlayer.copy(number = it.toIntOrNull() ?: editablePlayer.number) },
                        label = { Text("Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editablePlayer.position,
                        onValueChange = { editablePlayer = editablePlayer.copy(position = it) },
                        label = { Text("Position") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editablePlayer.age.toString(),
                        onValueChange = { editablePlayer = editablePlayer.copy(age = it.toIntOrNull() ?: editablePlayer.age) },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editablePlayer.height,
                        onValueChange = { editablePlayer = editablePlayer.copy(height = it) },
                        label = { Text("Height") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editablePlayer.weight,
                        onValueChange = { editablePlayer = editablePlayer.copy(weight = it) },
                        label = { Text("Weight") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showEditDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                onPlayerUpdate(editablePlayer)
                                showEditDialog = false
                            }
                        ) {
                            Text("Save")
                        }
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
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
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
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HockeyPlayerProfilePreview() {
    val samplePlayer = HockeyPlayer(
        name = "Connor McDavid",
        number = 97,
        position = "Center",
        team = "Edmonton Oilers",
        age = 26,
        height = "6'1\"",
        weight = "193 lbs",
        stats = PlayerStats(
            goals = 44,
            assists = 79,
            points = 123,
            plusMinus = 28,
            penaltyMinutes = 36
        ),
        imageRes = R.drawable.profileimage
    )

    PlayerProfile(player = samplePlayer)
}