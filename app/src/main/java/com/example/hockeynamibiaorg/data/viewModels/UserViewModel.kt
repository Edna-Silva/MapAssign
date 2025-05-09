package com.example.hockeynamibiaorg.data.viewModels

import com.example.hockeynamibiaorg.data.repositories.SessionManager


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.data.repositories.AuthService
import com.example.hockeynamibiaorg.data.repositories.UserService
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val userService = UserService()
    private val authService = AuthService()

    // LiveData for current user
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    // States for login process
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    init {
        // Initialize with session data if available
        if (sessionManager.isLoggedIn()) {
            _currentUser.value = sessionManager.getCurrentUser()
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = userService.getUserByEmail(email)

                if (user != null) {
                    // Save user to session
                    sessionManager.saveUserData(user)
                    _currentUser.value = user
                    _loginSuccess.value = true
                } else {
                    _errorMessage.value = "Invalid email or password"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Login failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun forgotPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        _isLoading.value = true

        viewModelScope.launch {
            if (authService.sendPasswordResetEmail(email)) {
                onSuccess()
            } else {
                onFailure("Failed to send reset email. Please try again.")
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        sessionManager.clearUserData()
        _currentUser.value = null
        _loginSuccess.value = false
    }

    fun getUserRoleDestination(): String {
        val role = _currentUser.value?.role?.lowercase() ?: return "player_home"

        return when (role) {
            "player" -> "player_home"
            "coach" -> "coach_home"
            else -> "player_home"
        }
    }

    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getCurrentUserEmail(): String? {
        return sessionManager.getUserEmail()
    }

    fun getCurrentUserRole(): String? {
        return sessionManager.getUserRole()
    }

    fun getCurrentUserFullName(): String? {
        return sessionManager.getUserFullName()
    }
}