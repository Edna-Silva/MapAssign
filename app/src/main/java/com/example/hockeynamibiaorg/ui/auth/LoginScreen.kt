package com.example.hockeynamibiaorg.ui.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.hockeynamibiaorg.R

import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.Navigation
//import com.example.hockeynamibiaorg.ui.common.PLAYER_MANAGEMENT
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
//import com.example.hockeynamibiaorg.ui.common.REMOVE_PLAYER
import com.example.hockeynamibiaorg.ui.common.SecondaryButton
import com.example.hockeynamibiaorg.ui.common.WelcomeScreen
import com.example.hockeynamibiaorg.ui.player.EventEntriesScreen
import com.example.hockeynamibiaorg.ui.player.HockeyPlayer
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen
import com.example.hockeynamibiaorg.ui.player.PlayerProfile
import com.example.hockeynamibiaorg.ui.player.PlayerStats
import com.example.myapplication.RemovePlayer
import com.example.hockeynamibiaorg.ui.theme.Purple80
// 1. First

// 2. Main NavHost that handles all navigation

// 3. Login Screen with proper navigation
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Player") }
    val roles = listOf("Player", "Coach")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login to Your Account",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(label = "Your Email")
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(label = "Password", isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))

            // Role Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    label = { Text("Select Role") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(text = role) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Login as $selectedRole",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    when (selectedRole) {
                        "Player" -> navController.navigate(Navigation.PlayerHome.route) {
                            popUpTo(Navigation.Login.route) { inclusive = true }
                        }
                        "Coach" -> navController.navigate(Navigation.CoachHome.route) {
                            popUpTo(Navigation.Login.route) { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Register",
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Navigation.Register.route) }
            )
        }
    }
}

// 4. PrimaryButton implementation
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Purple80),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

// 5. Sample data (keep your existing implementation)
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