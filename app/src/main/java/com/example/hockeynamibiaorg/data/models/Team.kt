package com.example.hockeynamibiaorg.data.models
data class Team(
    val id: String = "",
    val name: String = "",
    val ageGroup: String = "",
    val gender: String = "",
    val category: String = "",
    val coachId: String = "", // Link to the coach who created this team
    val players: List<String> = emptyList(), // List of player IDs
    val dateCreated: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)