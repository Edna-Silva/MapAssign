package com.example.hockeynamibiaorg.data.repositories


import android.content.Context
import android.content.SharedPreferences
import com.example.hockeynamibiaorg.data.models.User
import com.google.gson.Gson

/**
 * Manages user session data throughout the application
 */
class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "HockeyNamibiaPrefs", Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_ROLE = "user_role"
        const val KEY_USER_FULL_NAME = "user_full_name"
        const val KEY_USER_OBJECT = "user_object"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    /**
     * Saves user data to SharedPreferences when user logs in
     */
    fun saveUserData(user: User) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_ROLE, user.role)
            putString(KEY_USER_FULL_NAME, "${user.firstName} ${user.lastName}")
            putString(KEY_USER_OBJECT, gson.toJson(user))
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    /**
     * Retrieves the current user object
     */
    fun getCurrentUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER_OBJECT, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    /**
     * Checks if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Gets current user email
     */
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    /**
     * Gets current user role
     */
    fun getUserRole(): String? {
        return sharedPreferences.getString(KEY_USER_ROLE, null)
    }

    /**
     * Gets current user full name
     */
    fun getUserFullName(): String? {
        return sharedPreferences.getString(KEY_USER_FULL_NAME, null)
    }

    /**
     * Clears user data on logout
     */
    fun clearUserData() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}