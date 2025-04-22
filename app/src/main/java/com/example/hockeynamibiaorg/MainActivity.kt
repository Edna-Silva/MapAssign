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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.example.hockeynamibiaorg.ui.player.EventEntriesScreen
import com.example.hockeynamibiaorg.ui.player.HockeyPlayer
import com.example.hockeynamibiaorg.ui.player.PlayerHomeScreen
import com.example.hockeynamibiaorg.ui.player.PlayerProfile
import com.example.hockeynamibiaorg.ui.player.PlayerStats
import com.example.hockeynamibiaorg.ui.player.Screen

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
                   // NamibiaHockeyApp()
                    //MainScreen()
                    val navController: NavHostController = rememberNavController()
                    PlayerAppNavHost(navController)
                }
            }
        }
    }
}
@Composable
fun PlayerAppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.PlayerHome.route) {
        composable(Screen.PlayerHome.route) {
            PlayerHomeScreen(navController)
        }
        composable(Screen.PlayerEvents.route) {
            EventEntriesScreen(navController) // Your Profile Composable
        }
        composable(Screen.Profile.route) {
            PlayerProfile(samplePlayer) // Your Profile Composable
        }
    }
}


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
        imageRes = R.drawable.profileimage // Replace with actual image resource
    )


/*@RequiresApi(Build.VERSION_CODES.O)
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
            //CoachHomeContent(navController = navController)
            PlayerHomeScreen(navController = navController)
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
        composable("PlayersEvents") {
            EventEntriesScreen(navController = navController)
        }


    }
}*/
