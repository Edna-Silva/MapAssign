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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.ui.theme.HockeyNamibiaOrgTheme

import com.example.hockeynamibiaorg.ui.common.WelcomeScreen

import com.example.hockeynamibiaorg.ui.auth.LoginScreen
import com.example.hockeynamibiaorg.ui.auth.RegisterScreen
import com.example.hockeynamibiaorg.ui.auth.samplePlayer
import com.example.hockeynamibiaorg.ui.coach.CoachHomeContent
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerManagementScreen
import com.example.hockeynamibiaorg.ui.coach.TeamScreen
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.player.EventEntriesScreen
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen
import com.example.hockeynamibiaorg.ui.player.PlayerProfile
import com.example.myapplication.RemovePlayer
import com.google.firebase.Firebase
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
                    val context = LocalContext.current  // Get the correct context
                    val db = Firebase.firestore
                    // Create a new user with a first and last name
                    val user = hashMapOf(
                        "first" to "Ada",
                        "last" to "Lovelace",
                        "born" to 1815
                    )

// Add a new document with a generated ID
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
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

    NavHost(
        navController = navController,
        startDestination = Navigation.Welcome.route
    ) {
        // Auth screens

        composable(Navigation.Welcome.route) { WelcomeScreen(navController) }
        composable(Navigation.Login.route) { LoginScreen(navController) }
        composable(Navigation.Register.route) { RegisterScreen(navController) }

        // Player screens
        composable(Navigation.PlayerHome.route) { PlayerHomeScreen(navController) }
        composable(Navigation.PlayerEvents.route) { EventEntriesScreen(navController) }
        composable(Navigation.PlayerProfile.route) { PlayerProfile(samplePlayer) }

        // Coach screens
        composable(Navigation.CoachHome.route) { CoachHomeContent(navController) }
        composable(Navigation.Teams.route) { TeamScreen(navController) }
        composable(Navigation.Events.route) { EventManagementScreen(navController) }
        composable(Navigation.PlayerManagement.route) { PlayerManagementScreen(navController) }
        composable(Navigation.RemovePlayer.route) { RemovePlayer(navController) }
    }
}