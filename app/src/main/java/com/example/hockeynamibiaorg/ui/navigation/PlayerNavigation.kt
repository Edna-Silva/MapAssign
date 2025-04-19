package com.example.hockeynamibiaorg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hockeynamibiaorg.ui.coach.CoachHomeScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerDetailsScreen
import com.example.hockeynamibiaorg.ui.coach.TeamManagementScreen
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen

@Composable
fun PlayerNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "playerHome"
    ) {
        composable("playerHome") { PlayerHomeScreen(navController) }


    }
}

