package com.example.hockeynamibiaorg.data.repositories


import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth = FirebaseAuth.getInstance()

    /**
     * Creates a new user with email and password
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("AuthService", "Auth error: ${e.message}", e) //error occurs if anything wrong happens in the firebase
            false
        }
    }
    fun deleteUser() {
        auth.currentUser?.delete()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        } catch (e: Exception) {
            Log.e(TAG, "Sign in with email/password failed: ${e.message}", e)
            false
        }
    }
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Send password reset email failed: ${e.message}", e)
            false
        }
    }

    /**
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
    }



}