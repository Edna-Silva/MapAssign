package com.example.hockeynamibiaorg.data.models
data class User(
    val username: String = "",  // Primary key
    val fullName: String = "",
    val email: String = "",
    val role: String = "",      // "indoor_coach", "outdoor_coach", "indoor_player", "outdoor_player"
    val age: Int = 0,
    val gender: String = "",
    val category: String = "",  // "Indoor" or "Outdoor"
    val phone: String = "",
    val profileImageUrl: String = ""
) {
    // Empty constructor for Firebase
    constructor() : this("", "", "", "", 0, "", "", "")
}

