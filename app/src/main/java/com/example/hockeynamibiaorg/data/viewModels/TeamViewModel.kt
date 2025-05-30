
package com.example.hockeynamibiaorg.data.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.Event
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.data.models.Team
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TeamViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    // Load teams for the current coach only
    fun loadTeams() {
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid
            if (currentUserId == null) {
                Log.e("TeamViewModel", "No authenticated user")
                return@launch
            }

            db.collection("teams")
                .whereEqualTo("coachId", currentUserId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("TeamViewModel", "Error loading teams", error)
                        return@addSnapshotListener
                    }

                    val teamList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Team::class.java)?.copy(id = doc.id)
                    } ?: emptyList()

                    _teams.value = teamList
                }
        }
    }

    // Delete team with cascading deletion
    fun deleteTeam(teamId: String) {
        viewModelScope.launch {
            try {
                // Get the team first to access its players
                val teamDoc = db.collection("teams").document(teamId).get().await()
                val team = teamDoc.toObject(Team::class.java)

                if (team != null) {
                    // Remove team association from all players in this team
                    team.players.forEach { playerId ->
                        db.collection("players").document(playerId)
                            .update(
                                mapOf(
                                    "teamId" to ""
                                )
                            )
                    }

                    // Delete all events associated with this team
                    val eventsSnapshot = db.collection("events")
                        .whereEqualTo("teamId", teamId)
                        .get()
                        .await()

                    eventsSnapshot.documents.forEach { eventDoc ->
                        eventDoc.reference.delete()
                    }

                    // Finally delete the team
                    db.collection("teams").document(teamId).delete().await()

                    Log.d("TeamViewModel", "Team deleted successfully with cascading deletion")
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Error deleting team with cascading deletion", e)
            }
        }
    }

    fun registerTeam(
        name: String,
        ageGroup: String,
        gender: String,
        category: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val currentUserId = auth.currentUser?.uid
                if (currentUserId == null) {
                    onFailure(Exception("No authenticated user"))
                    return@launch
                }

                val newTeam = Team(
                    name = name,
                    ageGroup = ageGroup,
                    gender = gender,
                    category = category,
                    coachId = currentUserId // Link the team to the current coach
                )

                db.collection("teams")
                    .add(newTeam)
                    .addOnSuccessListener {
                        loadTeams() // Refresh the teams list
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    // Get all teams for a specific coach (useful for admin views)
    fun getCoachTeams(coachId: String): StateFlow<List<Team>> {
        val result = MutableStateFlow<List<Team>>(emptyList())

        viewModelScope.launch {
            db.collection("teams")
                .whereEqualTo("coachId", coachId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("TeamViewModel", "Error loading coach teams", error)
                        return@addSnapshotListener
                    }

                    val teamList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Team::class.java)?.copy(id = doc.id)
                    } ?: emptyList()

                    result.value = teamList
                }
        }

        return result
    }

    fun getPlayer(playerId: String): StateFlow<Player?> {
        val result = MutableStateFlow<Player?>(null)

        viewModelScope.launch {
            db.collection("players").document(playerId).get()
                .addOnSuccessListener { doc ->
                    result.value = doc.toObject(Player::class.java)?.copy(id = doc.id)
                }
        }

        return result
    }

    fun getTeamEvents(teamId: String): StateFlow<List<Event>> {
        val result = MutableStateFlow<List<Event>>(emptyList())

        viewModelScope.launch {
            db.collection("events")
                .whereEqualTo("teamId", teamId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener

                    val eventList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Event::class.java)?.copy(id = doc.id)
                    } ?: emptyList()

                    result.value = eventList
                }
        }

        return result
    }

    fun updatePlayerTeam(playerId: String, newTeamId: String) {
        viewModelScope.launch {
            try {
                // Update the player's teamId
                db.collection("players").document(playerId)
                    .update("teamId", newTeamId)
                    .await()

                // If moving to a new team, add player to the new team's players list
                if (newTeamId.isNotEmpty()) {
                    val teamDoc = db.collection("teams").document(newTeamId).get().await()
                    val team = teamDoc.toObject(Team::class.java)

                    if (team != null && !team.players.contains(playerId)) {
                        val updatedPlayers = team.players.toMutableList()
                        updatedPlayers.add(playerId)

                        db.collection("teams").document(newTeamId)
                            .update("players", updatedPlayers)
                            .await()
                    }
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Error updating player team", e)
            }
        }
    }

    fun addEvent(teamId: String, title: String, description: String, date: String, time: String, type: String) {
        viewModelScope.launch {
            val newEvent = Event(
                teamId = teamId,
                title = title,
                description = description,
                date = date,
                time = time,
                type = type
            )
            db.collection("events").add(newEvent)
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            db.collection("events").document(eventId).delete()
        }
    }

    // Add a player to a team
    fun addPlayerToTeam(teamId: String, playerId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        viewModelScope.launch {
            try {
                // First, remove player from any existing team
                removePlayerFromCurrentTeam(playerId)

                // Get the team document
                val teamDoc = db.collection("teams").document(teamId).get().await()
                val team = teamDoc.toObject(Team::class.java)

                if (team != null) {
                    // Add player to team's players list if not already there
                    if (!team.players.contains(playerId)) {
                        val updatedPlayers = team.players.toMutableList()
                        updatedPlayers.add(playerId)

                        // Update team document
                        db.collection("teams").document(teamId)
                            .update("players", updatedPlayers)
                            .await()
                    }

                    // Update player's teamId
                    db.collection("players").document(playerId)
                        .update("teamId", teamId)
                        .await()

                    onSuccess()
                } else {
                    onFailure(Exception("Team not found"))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    // Remove player from their current team
    private suspend fun removePlayerFromCurrentTeam(playerId: String) {
        try {
            // Get player's current team
            val playerDoc = db.collection("players").document(playerId).get().await()
            val player = playerDoc.toObject(Player::class.java)

            if (player != null && player.teamId.isNotEmpty()) {
                // Remove player from current team's players list
                val currentTeamDoc = db.collection("teams").document(player.teamId).get().await()
                val currentTeam = currentTeamDoc.toObject(Team::class.java)

                if (currentTeam != null) {
                    val updatedPlayers = currentTeam.players.toMutableList()
                    updatedPlayers.remove(playerId)

                    db.collection("teams").document(player.teamId)
                        .update("players", updatedPlayers)
                        .await()
                }
            }
        } catch (e: Exception) {
            Log.e("TeamViewModel", "Error removing player from current team", e)
        }
    }

    // Remove player from team
    fun removePlayerFromTeam(teamId: String, playerId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        viewModelScope.launch {
            try {
                // Remove player from team's players list
                val teamDoc = db.collection("teams").document(teamId).get().await()
                val team = teamDoc.toObject(Team::class.java)

                if (team != null) {
                    val updatedPlayers = team.players.toMutableList()
                    updatedPlayers.remove(playerId)

                    db.collection("teams").document(teamId)
                        .update("players", updatedPlayers)
                        .await()
                }

                // Clear player's teamId
                db.collection("players").document(playerId)
                    .update("teamId", "")
                    .await()

                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}