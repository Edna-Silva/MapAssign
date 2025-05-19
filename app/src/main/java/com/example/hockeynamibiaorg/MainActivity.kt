package com.example.hockeynamibiaorg

import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.data.repositories.SessionManager
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel
import com.example.hockeynamibiaorg.ui.auth.ForgotPasswordScreen
import com.example.hockeynamibiaorg.ui.theme.HockeyNamibiaOrgTheme

import com.example.hockeynamibiaorg.ui.common.WelcomeScreen
import com.example.hockeynamibiaorg.ui.auth.LoginScreen
import com.example.hockeynamibiaorg.ui.auth.RegisterScreen
 import com.example.hockeynamibiaorg.ui.coach.EditTeamScreen
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.TeamPlayersScreen
import com.example.hockeynamibiaorg.ui.coach.TeamScreen
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.player.EventEntriesScreen
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen


import com.example.hockeynamibiaorg.ui.coach.*
import com.example.hockeynamibiaorg.ui.player.PlayerProfileScreen
import com.example.hockeynamibiaorg.ui.player.PlayerTeamScreen
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val sessionManager = SessionManager(applicationContext)
        val startDestination = if (sessionManager.isLoggedIn()) {
            when (sessionManager.getUserRole()?.lowercase()) {
                "coach" -> Navigation.CoachHome.route
                "player" -> Navigation.PlayerHome.route
                else -> Navigation.Login.route
            }
        } else {
            Navigation.Welcome.route
        }

        setContent {
            HockeyNamibiaOrgTheme {
                val userViewModel: UserViewModel = viewModel()
                HockeyApp(startDestination = startDestination, userViewModel = userViewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HockeyApp(startDestination: String, userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Screens
        composable(Navigation.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Navigation.Login.route) {
            LoginScreen(navController)
        }
        composable(Navigation.Logout.route) {
            LoginScreen(navController)
        }
        composable(Navigation.Register.route) {
            RegisterScreen(navController)
        }
        composable(Navigation.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }

        // Player Screens
        composable(Navigation.PlayerHome.route) {
            PlayerHomeScreen(navController)
        }
        composable(Navigation.PlayerEvents.route) {
            EventEntriesScreen(navController)
        }
        val userId = userViewModel.currentUser.value?.id ?: ""
         composable(Navigation.PlayerTeam.route){
             PlayerTeamScreen(navController,userId)
         }

        composable("player_team/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
            PlayerTeamScreen(navController, playerId)
        }

        // Coach Screens
        composable(Navigation.CoachHome.route) {
            CoachHomeScreen(navController)
        }
        composable(Navigation.Teams.route) {
            TeamScreen(navController)
        }
        composable(Navigation.Events.route) {
            EventManagementScreen(navController)
        }

// Composable destination registration
        composable(Navigation.PlayerProfile.route){
            PlayerProfileScreen(userId, navController)
        }
        composable(
            "player_profile/{playerId}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
            PlayerProfileScreen(playerId, navController)
        }
        composable(Navigation.CoachProfile.route){
            CoachProfileScreen(userId,navController)
        }
        composable(
            "coach_profile/{coachId}") { backStackEntry ->
            val coachId = backStackEntry.arguments?.getString("coachId") ?: ""
            CoachProfileScreen(coachId, navController)
        }

        // Dynamic Routes
        composable("teamPlayers/{teamId}") { backStackEntry ->
            TeamPlayersScreen(
                navController,
                backStackEntry.arguments?.getString("teamId") ?: ""
            )
        }
        composable("editTeam/{teamId}") { backStackEntry ->
           EditTeamScreen(
               navController,
                backStackEntry.arguments?.getString("teamId") ?: ""
            )
            }
        }

    }
