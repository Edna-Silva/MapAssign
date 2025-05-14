
package com.example.hockeynamibiaorg.data.models
data class Event(
    val id: String = "",
    val teamId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val type: String = "", // "Tournament", "Camp", or "League"
    val location: String = ""
)
