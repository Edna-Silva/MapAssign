package com.example.hockeynamibiaorg.data.models

data class TeamMembership(
    val id: String = "", // Auto-generated
    val teamId: String = "",
    val playerId: String = "",
    val playerUsername: String = "",
    val playerName: String = "",
    val ageGroup: String = "",
    val joinDate: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", "")
}