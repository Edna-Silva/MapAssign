package com.example.hockeynamibiaorg.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel
import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
import com.example.hockeynamibiaorg.ui.common.SecondaryButton

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    // Form states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observe ViewModel states
    val isLoading by userViewModel.isLoading.observeAsState(false)
    val errorMessage by userViewModel.errorMessage.observeAsState(null)
    val loginSuccess by userViewModel.loginSuccess.observeAsState(false)
    val currentUser by userViewModel.currentUser.observeAsState()

    // Navigate on successful login
    LaunchedEffect(loginSuccess) {
        if (loginSuccess && currentUser != null) {
            val destination = when (currentUser?.role?.lowercase()) {
                "player" -> Navigation.PlayerHome.route
                "coach" -> Navigation.CoachHome.route
                else -> Navigation.PlayerHome.route
            }

            navController.navigate(destination) {
                popUpTo(Navigation.Login.route) { inclusive = true }
            }
        }
    }

    // Check if already logged in
    LaunchedEffect(Unit) {
        if (userViewModel.isLoggedIn()) {
            val destination = userViewModel.getUserRoleDestination()
            navController.navigate(destination) {
                popUpTo(Navigation.Login.route) { inclusive = true }
            }
        }
    }

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

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Signing In..." else "Login",
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            // Input validation
            when {
                email.isBlank() -> {
                    userViewModel._errorMessage.value = "Please fill in your email"
                }
                password.isBlank() -> {
                    userViewModel._errorMessage.value = "Please fill in your password"
                }
                password.length < 6 -> {
                    userViewModel._errorMessage.value = "Password should be at least 6 characters"
                }
                else -> {
                    // Call login function from ViewModel
                    userViewModel.login(email, password)
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
fun ForgotPasswordScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current

    // Form states
    var email by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Observe ViewModel states
    val isLoading by userViewModel.isLoading.observeAsState(false)
    val errorMessage by userViewModel.errorMessage.observeAsState(null)

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
            style = MaterialTheme.typography.bodyMedium
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
                userViewModel._errorMessage.value = "Please enter a valid email address"
                return@PrimaryButton
            }

            userViewModel.forgotPassword(
                email,
                onSuccess = {
                    successMessage = "Password reset email sent. Please check your inbox."
                },
                onFailure = { message ->
                    userViewModel._errorMessage.value = message
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "Back to Login",
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.popBackStack() }
        )
    }
}