package com.example.hockeynamibiaorg.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.Navigation
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
import com.example.hockeynamibiaorg.ui.common.SecondaryButton
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel
import com.example.hockeynamibiaorg.ui.viewmodels.RegistrationViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    // Define the gradient colors
    val navyBlue = Color(0xFF142143)
    val blue = Color(0xFF1a5d94)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(navyBlue, blue)
    )
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(navyBlue, blue)
    )

    // Form states
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Role dropdown
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Player") }
    val roles = listOf("Player", "Coach")

    // Define the colors
    val black = Color(0xFF000000)
    val lightLightBlue = Color(0xFFE6F0FF)

    // Observe ViewModel states
    val isLoading by registrationViewModel.isLoading.observeAsState(false)
    val errorMessage by registrationViewModel.errorMessage.observeAsState(null)
    val registrationSuccess by registrationViewModel.registrationSuccess.observeAsState(false)

    // Navigate on successful registration and login
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            userViewModel.login(email, password)
            navController.navigate(Navigation.Login.route) {
                popUpTo(Navigation.Register.route) { inclusive = true }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fill in your details to register",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // First Name field with rounded corners
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last Name field with rounded corners
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field with rounded corners
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number field with rounded corners
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Role dropdown with rounded cornerstext
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .menuAnchor(),
                    label = { Text("Role", color = black) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = black,
                        unfocusedTextColor = black,
                        containerColor = lightLightBlue,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role, color = black) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password field with rounded corners
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password field with rounded corners
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = black,
                    unfocusedTextColor = black,                    containerColor = lightLightBlue,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register button matching the login button style
            SecondaryButton(
                text = if (isLoading) "Creating Account..." else "Register",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    when {
                        firstName.isBlank() || lastName.isBlank() -> {
                            registrationViewModel.updateErrorMessage("Please enter your full name")
                        }
                        email.isBlank() -> {
                            registrationViewModel.updateErrorMessage("Please enter your email")
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            registrationViewModel.updateErrorMessage("Please enter a valid email address")
                        }
                        password.isBlank() -> {
                            registrationViewModel.updateErrorMessage("Please enter a password")
                        }
                        password.length < 6 -> {
                            registrationViewModel.updateErrorMessage("Password should be at least 6 characters")
                        }
                        password != confirmPassword -> {
                            registrationViewModel.updateErrorMessage("Passwords do not match")
                        }
                        else -> {
                            val user = User(
                                id = UUID.randomUUID().toString(),
                                email = email,
                                firstName = firstName,
                                lastName = lastName,
                                role = selectedRole.lowercase(),
                                phoneNumber = phoneNumber
                            )
                            registrationViewModel.registerUser(user, password)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Already have an account? Login",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(Navigation.Login.route) {
                        popUpTo(Navigation.Register.route) { inclusive = true }
                    }
                }
            )
        }
    }
}