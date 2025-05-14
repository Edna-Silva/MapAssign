package com.example.hockeynamibiaorg.data.models

data class Coach (
        val id: String = "",
        val ageGroup: String = "",
        val teamId: String = "",
        val stats:String="",
        val email: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val role: String = "player", // "player" or "coach"
        val phoneNumber: String = "",
        val profileImageUrl: String = "",
        val dateJoined: Long = System.currentTimeMillis(),
        val isActive: Boolean = true,


    )
