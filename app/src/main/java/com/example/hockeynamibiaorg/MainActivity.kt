package com.example.hockeynamibiaorg

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.ui.auth.ForgotPasswordScreen
import com.example.hockeynamibiaorg.ui.theme.HockeyNamibiaOrgTheme

import com.example.hockeynamibiaorg.ui.common.WelcomeScreen

import com.example.hockeynamibiaorg.ui.auth.LoginScreen
import com.example.hockeynamibiaorg.ui.auth.RegisterScreen
//import com.example.hockeynamibiaorg.ui.auth.samplePlayer
import com.example.hockeynamibiaorg.ui.coach.CoachHomeContent
import com.example.hockeynamibiaorg.ui.coach.EditTeamScreen
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerManagementScreen
import com.example.hockeynamibiaorg.ui.coach.TeamPlayersScreen
import com.example.hockeynamibiaorg.ui.coach.TeamScreen
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.player.EventEntriesScreen
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen
import com.example.hockeynamibiaorg.ui.player.PlayerProfile
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HockeyNamibiaOrgTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirebaseApp.initializeApp(this)

                    HockeyApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HockeyApp() {

    val navController = rememberNavController()
    val auth = Firebase.auth

    // Check if user is logged in when app starts
    LaunchedEffect(Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                // User is logged in, navigate to appropriate screen
                // You might want to store user role in Firestore or SharedPreferences
                navController.navigate(Navigation.PlayerHome.route) {
                    popUpTo(Navigation.Login.route) { inclusive = true }
                }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = Navigation.Welcome.route
    ) {
        // Auth screens

        composable(Navigation.Welcome.route) { WelcomeScreen(navController) }
        composable(Navigation.Login.route) { LoginScreen(navController) }
        composable(Navigation.Register.route) { RegisterScreen(navController) }
        composable(Navigation.ForgotPassword.route) { ForgotPasswordScreen(navController) }

        // Player screens
        composable(Navigation.PlayerHome.route) { PlayerHomeScreen(navController) }
        composable(Navigation.PlayerEvents.route) { EventEntriesScreen(navController) }
      //  composable(Navigation.PlayerProfile.route) { PlayerProfile(user) }

        // Coach screens
        composable(Navigation.CoachHome.route) { CoachHomeContent(navController) }
        composable(Navigation.Teams.route) { TeamScreen(navController) }
        composable(Navigation.Events.route) { EventManagementScreen(navController) }
        composable(Navigation.PlayerManagement.route) { PlayerManagementScreen(navController) }
       // composable(Navigation.RemovePlayer.route) { RemovePlayer(navController) }
        composable("teams") { TeamScreen(navController) }
        composable("teamPlayers/{teamId}") { backStackEntry ->
            TeamPlayersScreen(navController, backStackEntry.arguments?.getString("teamId") ?: "")
        }
        composable("editTeam/{teamId}") { backStackEntry ->
            EditTeamScreen(navController, backStackEntry.arguments?.getString("teamId") ?: "")
        }
    }
}