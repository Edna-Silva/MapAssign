package com.example.hockeynamibiaorg.ui.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
import com.example.hockeynamibiaorg.ui.common.SecondaryButton

// 1. First

// 2. Main NavHost that handles all navigation

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

import com.example.hockeynamibiaorg.data.repositories.AuthService
import com.example.hockeynamibiaorg.data.repositories.UserService

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val authService = remember { AuthService() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userService = remember { UserService() }

    // Form states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Role dropdown
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Player") }
    val roles = listOf("Player", "Coach")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Login to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        AppTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                label = { Text("Select Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(text = role) },
                        onClick = {
                            selectedRole = role
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Signing In..." else "Login",
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (email.isBlank() || password.isBlank()) {
                errorMessage = "Please fill in all fields"
                return@PrimaryButton
            }

            if (password.length < 6) {
                errorMessage = "Password should be at least 6 characters"
                return@PrimaryButton
            }

            isLoading = true
            errorMessage = null

            scope.launch {
                when (val result = userService.getUserByEmail(email)) {
                    null -> {
                        errorMessage = "Invalid email or password"
                        isLoading = false
                    }
                    else -> {
                        // Navigate based on role (use the role from Firestore or selectedRole)
                        val destination = when (result.role.lowercase()) {
                            "player" -> Navigation.PlayerHome.route
                            "coach" -> Navigation.CoachHome.route

                            else -> Navigation.PlayerHome.route
                        }

                        navController.navigate(destination) {
                            popUpTo(Navigation.Login.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "Don't have an account? Register",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Navigation.Register.route)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate(Navigation.ForgotPassword.route)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Forgot Password?",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val authService = remember { AuthService() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter your email to receive a password reset link",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        AppTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Sending..." else "Send Reset Link",
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.isNotBlank()
        ) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorMessage = "Please enter a valid email address"
                return@PrimaryButton
            }

            isLoading = true
            errorMessage = null
            successMessage = null

            scope.launch {
                if (authService.sendPasswordResetEmail(email)) {
                    successMessage = "Password reset email sent. Please check your inbox."
                } else {
                    errorMessage = "Failed to send reset email. Please try again."
                }
                isLoading = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "Back to Login",
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.popBackStack() }
        )
    }
}