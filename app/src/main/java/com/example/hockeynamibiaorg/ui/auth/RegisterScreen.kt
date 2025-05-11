package com.example.hockeynamibiaorg.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.firebase.auth.FirebaseAuth
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    registrationViewModel: RegistrationViewModel = viewModel()
) {

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

    // Observe ViewModel states
    val isLoading by registrationViewModel.isLoading.observeAsState(false)
    val errorMessage by registrationViewModel.errorMessage.observeAsState(null)
    val registrationSuccess by registrationViewModel.registrationSuccess.observeAsState(false)

    // Navigate on successful registration and login
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            // Log in the user
            userViewModel.login(email, password)
            navController.navigate(Navigation.Login.route) {
                popUpTo(Navigation.Register.route) { inclusive = true }
            }
        }
    }

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
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fill in your details to register",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        AppTextField(
            label = "First Name",
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.weight(1f)
        )

        AppTextField(
            label = "Last Name",
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.weight(1f)
        )


        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            label = "Phone Number",
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text("Role") }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            selectedRole = role
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Creating Account..." else "Register",
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            // Validate input
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
                    // Create user object
                    val user = User(
                        id = UUID.randomUUID().toString(),
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        role = selectedRole.lowercase(),
                        phoneNumber = phoneNumber
                    )

                    // Register user
                    registrationViewModel.registerUser(user, password)

                }
            }
        }

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