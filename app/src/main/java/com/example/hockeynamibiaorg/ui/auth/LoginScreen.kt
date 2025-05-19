package com.example.hockeynamibiaorg.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel
import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.common.SecondaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

// Define the colors from the palette
val navyBlue = Color(0xFF142143)
val blue = Color(0xFF1a5d94)
val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)
val lightGray = Color(0xFFe4e4e4)
val buttonGradient = Brush.horizontalGradient(
    colors = listOf(navyBlue, blue)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    // Define the gradient
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(navyBlue, blue)
    )

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
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
                fontSize = 24.sp,
                color = white
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = white.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            val lightLightBlue = Color(0xFFE6F0FF)

            // White fields with black text
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,
                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = black) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,
                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Aligned buttons column
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Login button styled to match Register button in screenshot
                Button(
                    onClick = {
                        when {
                            email.isBlank() -> userViewModel._errorMessage.value = "Please fill in your email"
                            password.isBlank() -> userViewModel._errorMessage.value = "Please fill in your password"
                            password.length < 6 -> userViewModel._errorMessage.value = "Password should be at least 6 characters"
                            else -> userViewModel.login(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1a3771)  // Darker blue as seen in the image
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = if (isLoading) "Signing In..." else "Login",
                        color = white,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }

                // Register button
                SecondaryButton(
                    text = "Don't have an account? Register",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(Navigation.Register.route) }
                )

                // Forgot password styled as button with same shape
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF2A7DBD), // Optional: use your blue tone
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Navigation.ForgotPassword.route)
                        }
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally) // Optional for layout alignment
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(navyBlue, blue)
    )

    // Form states
    var email by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Observe ViewModel states
    val isLoading by userViewModel.isLoading.observeAsState(false)
    val errorMessage by userViewModel.errorMessage.observeAsState(null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
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
                fontSize = 24.sp,
                color = white
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter your email to receive a password reset link",
                style = MaterialTheme.typography.bodyMedium,
                color = white.copy(alpha = 0.7f)
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
            val lightLightBlue = Color(0xFFE6F0FF)

            // White field with black text
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,
                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Aligned buttons with consistent styling
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Send Reset Link button with gradient
                Button(
                    onClick = {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            userViewModel._errorMessage.value = "Please enter a valid email address"
                            return@Button
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading && email.isNotBlank()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = buttonGradient)
                            .clip(MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isLoading) "Sending..." else "Send Reset Link",
                            color = white,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Back to Login button
                SecondaryButton(
                    text = "Back to Login",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.popBackStack() }
                )
            }
        }
    }
}