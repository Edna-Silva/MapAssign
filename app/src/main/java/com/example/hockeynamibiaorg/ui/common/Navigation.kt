
package com.example.hockeynamibiaorg.ui.common
sealed class Navigation(val route: String) {
    object Welcome : Navigation("welcome")
    object Login : Navigation("login")
    object Register : Navigation("register")
    object ForgotPassword : Navigation("forgot_password")
    object Logout : Navigation("logout")


    // Player routes
    object PlayerHome : Navigation("player_home")
    object PlayerEvents : Navigation("player_events")
    object PlayerProfile : Navigation("player_profile")
    object PlayerTeam: Navigation("player_team")

    // Coach routes
    object CoachHome : Navigation("coach_home")
    object Teams : Navigation("teams")
    object Events : Navigation("coach_events")
    object CoachProfile : Navigation("coach_profile")

    object PlayerManagement : Navigation("player_management")

}