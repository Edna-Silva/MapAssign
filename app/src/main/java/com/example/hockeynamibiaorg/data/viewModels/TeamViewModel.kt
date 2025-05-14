package com.example.hockeynamibiaorg.data.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.Event
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.data.models.Team
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    fun loadTeams() {
        viewModelScope.launch {
            db.collection("teams")
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

    fun deleteTeam(teamId: String) {
        viewModelScope.launch {
            try {
                db.collection("teams").document(teamId).delete()
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Error deleting team", e)
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
                val newTeam = Team(
                    name = name,
                    ageGroup = ageGroup,
                    gender = gender,
                    category = category,
                    //coachId = auth.currentUser.toString(),
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
            db.collection("players").document(playerId).update("teamId", newTeamId)
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
}