package com.example.hockeynamibiaorg.data.models



data class User(
    val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val role: String = "player", // "player" or "coach"
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val dateJoined: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)