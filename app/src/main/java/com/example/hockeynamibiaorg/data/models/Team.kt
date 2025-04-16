package com.example.hockeynamibiaorg.data.models
data class Team(
    val id: String = "", // Auto-generated
    val name: String = "",
    val ageGroup: String = "",
    val gender: String = "",
    val category: String = "", // Indoor/Outdoor
    val coachId: String = "",
    val playerCount: Int = 0,
    val maxPlayers: Int = 11
) {
    constructor() : this("", "", "", "", "", "", 0, 11)
}

