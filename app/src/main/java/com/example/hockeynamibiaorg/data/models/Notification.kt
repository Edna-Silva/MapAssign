package com.example.hockeynamibiaorg.data.models

data class Notification(
    val id: String = "", // Auto-generated
    val title: String = "",
    val message: String = "",
    val type: String = "", // Event, Deadline, Announcement
    val relatedId: String = "", // ID of related item (event, etc.)
    val timestamp: Long = System.currentTimeMillis(),
    val recipientIds: List<String> = emptyList(), // User IDs or "all"
    val read: Boolean = false
) {
    constructor() : this("", "", "", "", "", 0, emptyList())
}
