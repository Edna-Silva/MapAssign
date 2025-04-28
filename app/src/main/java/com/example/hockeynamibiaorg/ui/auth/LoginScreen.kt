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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.ui.coach.CoachHomeContent
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerManagementScreen
import com.example.hockeynamibiaorg.ui.coach.TeamScreen
import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.PLAYER_MANAGEMENT
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
import com.example.hockeynamibiaorg.ui.common.REMOVE_PLAYER
import com.example.hockeynamibiaorg.ui.common.SecondaryButton
import com.example.hockeynamibiaorg.ui.common.WelcomeScreen
import com.example.myapplication.RemovePlayer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    // Role selection state
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Player") }
    val roles = listOf("Player", "Coach")

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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

            //Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),


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
                modifier = Modifier.fillMaxWidth()
            ) {
                if(selectedRole=="Player"){
                // You can use selectedRole here for different navigation logic if needed
                    CoachNav()

            //    navController.navigate("home") {
              //      popUpTo("welcome") { inclusive = true }
                //}
                }
                    else{
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Register",
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate("register")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CoachNav(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            CoachHomeContent(navController = navController)
        }
        composable("teams") {
            TeamScreen(navController = navController)
        }
        composable("events") {
            EventManagementScreen(navController = navController)
        }
        composable("players") {
            PlayerManagementScreen(navController = navController)
        }
        composable(PLAYER_MANAGEMENT) {
            PlayerManagementScreen(navController)
        }
        // composable(ASSIGN_PLAYER) {
        //   AssignPlayerScreen(navController)
        //}
        composable(REMOVE_PLAYER) {

            RemovePlayer(navController)
        }
    }
}
