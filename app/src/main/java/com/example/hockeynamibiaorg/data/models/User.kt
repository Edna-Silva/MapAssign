package com.example.hockeynamibiaorg.data.models

import com.google.firebase.Timestamp



data class User(
    val id: String = "",
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "player",
    val age: Int = 0,
    val gender: String = "",
    val category: String = "",
    val ageGroup: String = "",
    val phone: String = "",
    val password: String ="",
    val profileImageUrl: String = ""
){
    // Empty constructor for Firebase
   // constructor() : this("", "", "", "", "", 0, "", "", "")
}
