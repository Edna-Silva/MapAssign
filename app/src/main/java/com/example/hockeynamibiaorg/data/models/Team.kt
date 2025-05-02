package com.example.hockeynamibiaorg.data.models
data class Team(
    val id: String = "",
    val name: String = "",
    val ageGroup: String = "",
    val category: String = "", // "Indoor", "Outdoor", or "Mixed"
    val coachId: String = "", // ID of the coach who created the team
    val players: List<String> = emptyList() // List of player IDs
)
