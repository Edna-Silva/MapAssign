package com.example.hockeynamibiaorg.ui.player

sealed class Screen(val route: String) {
    object PlayerHome : Screen("player_home")
    object Profile : Screen("profile")
    object PlayerEvents : Screen("PlayerEvents")
}
