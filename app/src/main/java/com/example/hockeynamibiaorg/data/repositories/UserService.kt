package com.example.hockeynamibiaorg.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.NavController
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
            // Add to People collection with email as document ID
            usersCollection.document(user.email).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create user failed: ${e.message}", e)
            false
        }
    }

    suspend fun createPlayer(user: User): Boolean {
        return try {
            // Add to players collection with email as document ID
            playersCollection.document(user.email).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create player failed: ${e.message}", e)
            false
        }
    }

    suspend fun createCoach(user: User): Boolean {
        return try {
            // Add to coaches collection with email as document ID
            coachesCollection.document(user.email).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create coach failed: ${e.message}", e)
            false
        }
    }

    suspend fun registerUser(user: User): Boolean {
        return try {
            // First create in People collection
            val userCreated = createUser(user)
            if (!userCreated) return false

            // Then create in appropriate role collection
            when (user.role.lowercase()) {
                "player" -> createPlayer(user)
                "coach" -> createCoach(user)
                else -> false
            }
        } catch (e: Exception) {
            Log.e("UserService", "Registration failed: ${e.message}", e)
            false
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            val document = usersCollection.document(email).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserService", "Get user by email failed: ${e.message}", e)
            null
        }
    }

    suspend fun getUserById(userId: String): User? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserService", "Get user by ID failed: ${e.message}", e)
            null
        }
    }
}