package com.example.hockeynamibiaorg.data.repositories

import android.util.Log
import com.example.hockeynamibiaorg.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


import com.google.firebase.firestore.FirebaseFirestore


class AuthService {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    // Check if user is already logged in
    fun getCurrentUser() = auth.currentUser

    // Email/password login
    suspend fun loginWithEmail(email: String, password: String): User? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return null

            // Get user data from Firestore
            val document = db.collection("People").document(email).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthService", "Login failed", e)
            null
        }
    }

    // Email/password registration
    suspend fun registerWithEmail(
        email: String,
        password: String,
        userData: User
    ): Boolean {
        return try {
            // 1. Create auth user
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return false

            // 2. Create user document in Firestore
            val user = userData.copy(id = userId)
            db.collection("People").document(userId).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("AuthService", "Registration failed", e)
            false
        }
    }

    // Password reset
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("AuthService", "Password reset failed", e)
            false
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }
}
/*class AuthService {
    private val auth = Firebase.auth

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}")
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun signOut() {
        auth.signOut()
    }

   suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            Log.e("AuthService", "Registration failed: ${e.message}", e)
            null
        }
    }

    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            Log.e("AuthService", "Login failed: ${e.message}", e)
            null
        }
    }



    fun logout() {
        auth.signOut()
    }

}*/