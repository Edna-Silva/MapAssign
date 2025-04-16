package com.example.hockeynamibiaorg.data.models
data class Event(
    val id: String = "", // Auto-generated
    val title: String = "",
    val description: String = "",
    val type: String = "", // "Training" or "Match" or "Other"
    val startTime: Long = 0,
    val endTime: Long = 0,
    val location: String = "",
    val teamIds: List<String> = emptyList(), // Teams this event applies to
    val createdBy: String = "", // Coach ID
    val notificationSent: Boolean = false
) {
    constructor() : this("", "", "", "", 0, 0, "", emptyList(), "")
}
