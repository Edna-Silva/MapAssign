package com.example.hockeynamibiaorg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.ui.navigation.CoachNavigation
import com.example.hockeynamibiaorg.ui.navigation.PlayerNavigation
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.example.hockeynamibiaorg.ui.theme.HockeyNamibiaOrgTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.firestore // Initialize Firebase

        setContent {
            HockeyNamibiaOrgTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HockeyNamibiaApp()
                }
            }
        }
    }
}

@Composable
fun HockeyNamibiaApp() {
    val navController = rememberNavController()
    PlayerNavigation(navController)
}