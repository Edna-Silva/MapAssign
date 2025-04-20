package com.example.hockeynamibiaorg.ui.common

// Define your coach navigation routes
sealed class Navigation(val route: String) {
    object Home : Navigation("home")
    object Teams : Navigation("teams")
    object Players : Navigation("players")
    object Events : Navigation("events")
}

// Similarly for PlayerNavigation.kt