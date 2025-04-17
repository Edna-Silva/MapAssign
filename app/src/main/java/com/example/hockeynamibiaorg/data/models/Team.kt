package com.example.hockeynamibiaorg.data.models
data class Team(
    val id: String = "",
    val name: String = "",
    val ageGroup: String = "",
    val gender: String = "",
    val hockeyType: String = "",
    val playerIds: List<String> = emptyList()
)
