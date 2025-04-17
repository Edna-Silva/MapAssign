package com.example.hockeynamibiaorg.ui.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CoachHomeScreen(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome Coach!", style = MaterialTheme.typography.headlineLarge)

            Button(
                onClick = { navController.navigate("teamManagement") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Teams")
            }

            Button(
                onClick = { navController.navigate("teamRegistration") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register New Team")
            }
        }
    }
}