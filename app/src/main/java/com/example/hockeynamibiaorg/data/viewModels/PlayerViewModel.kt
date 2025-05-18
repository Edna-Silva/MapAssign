package com.example.hockeynamibiaorg.data.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.hockeynamibiaorg.data.models.Player
import com.google.firebase.firestore.FirebaseFirestore

class PlayerProfileViewModel : ViewModel() {
    var player by mutableStateOf<Player?>(null)
    var isLoading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)
    private val db = FirebaseFirestore.getInstance()

    fun loadPlayerProfile(playerId: String) {
        isLoading = true
        error = null

        db.collection("players")
            .document(playerId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    player = document.toObject(Player::class.java)
                } else {
                    error = "Player not found"
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                error = "Error loading player: ${e.message}"
                isLoading = false
            }
    }

    fun updatePlayer(updatedPlayer: Player) {
        isLoading = true
        db.collection("players")
            .document(updatedPlayer.id)
            .set(updatedPlayer)
            .addOnSuccessListener {
                player = updatedPlayer
                isLoading = false
            }
            .addOnFailureListener { e ->
                error = "Failed to update: ${e.message}"
                isLoading = false
            }
    }
}