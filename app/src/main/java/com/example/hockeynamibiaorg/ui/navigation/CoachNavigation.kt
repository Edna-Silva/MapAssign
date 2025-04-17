package com.example.hockeynamibiaorg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hockeynamibiaorg.ui.coach.CoachHomeScreen
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerDetailsScreen
import com.example.hockeynamibiaorg.ui.coach.TeamManagementScreen


import com.example.hockeynamibiaorg.ui.coach.TeamRegistrationScreen

@Composable
fun CoachNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "coachHome"
    ) {
        composable("coachHome") { CoachHomeScreen(navController) }
        composable("teamManagement") { TeamManagementScreen(navController) }
        composable("playerDetails/{playerId}") { backStackEntry ->
            PlayerDetailsScreen(
                navController = navController,
                playerId = backStackEntry.arguments?.getString("playerId") ?: ""
            )
        }
        composable("teamRegistration") { TeamRegistrationScreen(navController) }
        composable("eventManagement/{teamId}") { backStackEntry ->
            EventManagementScreen(
                navController = navController,
                teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            )
        }
    }
}