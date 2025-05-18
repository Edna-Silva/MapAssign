package com.example.hockeynamibiaorg.data.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.hockeynamibiaorg.data.models.Coach
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CoachProfileViewModel : ViewModel() {
    var coach by mutableStateOf<Coach?>(null)
    var isLoading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)
    var teamNames by mutableStateOf<List<String>>(emptyList())
    private val db = FirebaseFirestore.getInstance()

    fun loadCoachProfile(coachId: String) {
        isLoading = true
        error = null

        db.collection("coaches")
            .document(coachId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    coach = document.toObject(Coach::class.java)
                    loadTeamNames(coach?.teamId ?: "")
                } else {
                    error = "Coach not found"
                    isLoading = false
                }
            }
            .addOnFailureListener { e ->
                error = "Error loading coach: ${e.message}"
                isLoading = false
            }
    }

    fun updateCoach(updatedCoach: Coach) {
        isLoading = true
        db.collection("coaches")
            .document(updatedCoach.id)
            .set(updatedCoach)
            .addOnSuccessListener {
                coach = updatedCoach
                loadTeamNames(updatedCoach.teamId)
            }
            .addOnFailureListener { e ->
                error = "Failed to update: ${e.message}"
                isLoading = false
            }
    }

    private fun loadTeamNames(teamIds: String) {
        if (teamIds.isEmpty()) {
            teamNames = emptyList()
            isLoading = false
            return
        }

        val teamIdList = teamIds.split(",").map { it.trim() }
        val namesList = mutableListOf<String>()
        var loadedCount = 0

        for (teamId in teamIdList) {
            db.collection("teams")
                .document(teamId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val teamName = document.getString("name") ?: "Unknown Team"
                        namesList.add(teamName)
                    } else {
                        namesList.add("Team Not Found")
                    }

                    loadedCount++
                    if (loadedCount == teamIdList.size) {
                        teamNames = namesList
                        isLoading = false
                    }
                }
                .addOnFailureListener { e ->
                    namesList.add("Error Loading Team")
                    loadedCount++
                    if (loadedCount == teamIdList.size) {
                        teamNames = namesList
                        isLoading = false
                    }
                }
        }
    }
}