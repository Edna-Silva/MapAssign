package com.example.hockeynamibiaorg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.ui.coach.CoachHomeContent
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerDetailsScreen
import com.example.hockeynamibiaorg.ui.coach.TeamManagementScreen


import com.example.hockeynamibiaorg.ui.coach.TeamRegistrationScreen

@Preview(showBackground = true)
@Composable
fun PreviewAll() {
    val navController = rememberNavController()
    CoachNavigation(navController)
}
@Composable
fun CoachNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "coachHome"
    ) {
        composable("coachHome") { CoachHomeContent(navController) }
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