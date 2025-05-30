package com.example.hockeynamibiaorg.data.repositories


import android.util.Log
import com.example.hockeynamibiaorg.data.models.Coach
import com.example.hockeynamibiaorg.data.models.Player
import com.example.hockeynamibiaorg.data.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserService {
    private val db = Firebase.firestore
    private val usersCollection = db.collection("People")
    private val playersCollection = db.collection("players")
    private val coachesCollection = db.collection("coaches")

    suspend fun createUser(user: User): Boolean {
        return try {
            usersCollection.document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create user failed: ${e.message}", e)
            false
        }
    }

    suspend fun createPlayer(player: Player): Boolean {
        return try {
            playersCollection.document(player.id).set(player).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create player failed: ${e.message}", e)
            false
        }
    }

    suspend fun createCoach(coach: Coach): Boolean {
        return try {
            coachesCollection.document(coach.id).set(coach).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create coach failed: ${e.message}", e)
            false
        }
    }

    suspend fun registerUser(user: User): Boolean {
        return try {
            val userCreated = createUser(user)
            if (!userCreated) return false

            when (user.role.lowercase()) {
                "player" -> {
                    val player = Player(
                        id = user.id,
                        email = user.email,
                        firstName= user.firstName,
                        lastName = user.lastName,
                        phoneNumber = user.phoneNumber,
                        ageGroup = "",
                        teamId = "",
                        stats = "",
                        goals = "",
                        points = "",
                        profileImageUrl = "",
                        //coachId = "",
                    )
                    createPlayer(player)
                }
                "coach" -> {
                    val coach = Coach(
                        id = user.id,
                        email = user.email,
                        firstName= user.firstName,
                        lastName = user.lastName,
                        phoneNumber = user.phoneNumber,
                        ageGroup = "",
                        teamId = "",
                        stats = "",
                        profileImageUrl = "",
                    )
                    createCoach(coach)
                }
                else -> true
            }
        } catch (e: Exception) {
            Log.e("UserService", "Registration failed: ${e.message}", e)
            false
        }
    }

    //  check for duplicates before registration
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserService", "Get user by email failed: ${e.message}", e)
            null
        }
    }



}