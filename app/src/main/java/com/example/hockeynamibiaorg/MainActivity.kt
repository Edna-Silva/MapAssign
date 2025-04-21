package com.example.hockeynamibiaorg

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.hockey.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.navigation.CoachNavigation
import com.example.hockeynamibiaorg.ui.navigation.PlayerNavigation
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.example.hockeynamibiaorg.ui.theme.HockeyNamibiaOrgTheme

import com.example.hockeynamibiaorg.ui.common.WelcomeScreen

import com.example.hockeynamibiaorg.ui.auth.LoginScreen
import com.example.hockeynamibiaorg.ui.auth.RegisterScreen
import com.example.hockeynamibiaorg.ui.coach.CoachHomeContent
import com.example.hockeynamibiaorg.ui.coach.EventManagementScreen
import com.example.hockeynamibiaorg.ui.coach.PlayerDetailsScreen
import com.example.hockeynamibiaorg.ui.coach.TeamScreen
import com.example.hockeynamibiaorg.ui.common.WelcomeScreen
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen

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
                    NamibiaHockeyApp()
                    //MainScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewAll() {

    NamibiaHockeyApp()
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NamibiaHockeyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            CoachHomeContent(navController = navController)
        }
        composable("teams") {
            TeamScreen(navController = navController)
        }
       composable("events") {
            EventManagementScreen(navController = navController)
        }
        composable("PlayerHomeScreen") {
            PlayerHomeScreen(navController = navController)
        }
        composable("players") {
            PlayerDetailsScreen(navController = navController,"78")
        }


    }
}
