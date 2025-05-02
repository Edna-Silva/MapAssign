package com.example.hockeynamibiaorg.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.hockeynamibiaorg.data.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserService {
    private val db = Firebase.firestore
     val usersCollection = db.collection("People")

    suspend fun createUser(user: User): Boolean {
        return try {
            // Save user data with user ID as the document ID
            usersCollection.add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            true
        } catch (e: Exception) {
            Log.e("UserService", "Create user failed: ${e.message}", e)
            false
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            val query = usersCollection.whereEqualTo("email", email).limit(1).get().await()
            if (!query.isEmpty) {
                query.documents.first().toObject<User>()
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
                document.toObject<User>()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserService", "Get user failed: ${e.message}", e)
            null
        }
    }

    /*suspend fun updateUser(user: User): Boolean {
        return try {
            usersCollection.document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Update user failed: ${e.message}", e)
            false
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = usersCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject<User>() }
        } catch (e: Exception) {
            Log.e("UserService", "Get all users failed: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getUsersByRole(role: String): List<User> {
        return try {
            val snapshot = usersCollection.whereEqualTo("role", role).get().await()
            snapshot.documents.mapNotNull { it.toObject<User>() }
        } catch (e: Exception) {
            Log.e("UserService", "Get users by role failed: ${e.message}", e)
            emptyList()
        }
    }*/
}