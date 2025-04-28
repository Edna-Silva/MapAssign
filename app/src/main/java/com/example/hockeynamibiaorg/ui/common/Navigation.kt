
package com.example.hockeynamibiaorg.ui.common

// Define your coach navigation routes
sealed class Navigation(val route: String) {
    object Home : Navigation("home")
    object Teams : Navigation("teams")
    object Players : Navigation("players")
    object Events : Navigation("events")
}

// Similarly for PlayerNavigation.kt
const val PLAYER_MANAGEMENT = "playerManagement"
const val ASSIGN_PLAYER = "assignPlayer"
const val REMOVE_PLAYER = "removePlayer"

const val UPDATE_PLAYER = "updatePlayer/{playerId}"
