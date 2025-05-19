package com.example.hockeynamibiaorg.ui.common
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun HockeyBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar(modifier = Modifier.height(64.dp)) {
        val navItems = listOf(
            Navigation.CoachHome,
            Navigation.Teams,
            Navigation.PlayerManagement,
            Navigation.Events
        )

        navItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        Navigation.CoachHome -> Icon(Icons.Default.Home, contentDescription = "Home")
                        Navigation.Teams -> Icon(Icons.Default.Notifications, contentDescription = "Teams")
                        Navigation.PlayerManagement -> Icon(Icons.Default.Person, contentDescription = "Players")
                        Navigation.Events -> Icon(Icons.Default.Search, contentDescription = "Events")
                        Navigation.Login -> TODO()
                        Navigation.PlayerEvents -> TODO()
                        Navigation.PlayerHome -> TODO()
                        Navigation.PlayerProfile -> TODO()
                        Navigation.Register -> TODO()
                        Navigation.Welcome -> TODO()
                        Navigation.ForgotPassword -> TODO()
                        Navigation.Logout -> TODO()
                        Navigation.PlayerTeam -> TODO()
                        Navigation.CoachProfile -> TODO()
                        Navigation.UserCommon -> TODO()
                    }
                },
                label = { Text(screen.route.substringAfter("_").replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}