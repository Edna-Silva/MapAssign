package com.example.hockeynamibiaorg.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.data.repositories.AuthService
import com.example.hockeynamibiaorg.data.repositories.UserService
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val userService = UserService()
    private val authService = AuthService()

    // States for registration process
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    fun registerUser(user: User, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // First check if email already exists
                val existingUser = userService.getUserByEmail(user.email)
                if (existingUser != null) {
                    _errorMessage.value = "Email already registered. Please login or use a different email."
                    _isLoading.value = false
                    return@launch
                }

                // Create authentication account
                val authSuccess = authService.createUserWithEmailAndPassword(user.email, password)
                if (!authSuccess) {
                    _errorMessage.value = "Failed to create authentication account. Please try again."
                    _isLoading.value = false
                    return@launch
                }

                // Register user in Firestore
                val registrationSuccess = userService.registerUser(user)
                if (registrationSuccess) {
                    _registrationSuccess.value = true

                } else {
                    _errorMessage.value = "Failed to save user information. Please try again."
                    // Clean up auth account if Firestore fails
                    authService.deleteUser()
                }
            } catch (e: Exception) {
                Log.e("Registration", "Exception during registration", e)
                _errorMessage.value = e.message ?: "Registration failed. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateErrorMessage(message: String?) {
        _errorMessage.value = message
    }
}


