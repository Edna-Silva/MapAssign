package com.example.hockeynamibiaorg.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.data.models.User
import com.example.hockeynamibiaorg.data.repositories.AuthService
import com.example.hockeynamibiaorg.data.repositories.UserService
import com.example.hockeynamibiaorg.ui.common.AppTextField
import com.example.hockeynamibiaorg.ui.common.PrimaryButton
import com.example.hockeynamibiaorg.ui.common.SecondaryButton
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // Services
    val authService = remember { AuthService() }
    val userService = remember { UserService() }

    // Coroutine scope
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Form states
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Role dropdown
    var expandedRole by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Player") }
    val roles = listOf("Player", "Coach")

    // Age group and category dropdowns
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf("U-18") }
    var selectedCategory by remember { mutableStateOf("Indoor") }
    val ageGroups = listOf("U-18", "U-20", "Senior")
    val categories = listOf("Indoor", "Outdoor", "Mixed")

    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Your Name",
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Your Email",
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender dropdown
            ExposedDropdownMenuBox(
                expanded = expandedRole,
                onExpandedChange = { expandedRole = !expandedRole },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    label = { Text("Select Role") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedRole,
                    onDismissRequest = { expandedRole = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(text = role) },
                            onClick = {
                                selectedRole = role
                                expandedRole = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender field
            AppTextField(
                label = "Gender",
                value = gender,
                onValueChange = { gender = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age Group Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedGroup,
                onExpandedChange = { expandedGroup = !expandedGroup },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedGroup,
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    label = { Text("Select Age Group") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedGroup,
                    onDismissRequest = { expandedGroup = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ageGroups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(text = group) },
                            onClick = {
                                selectedGroup = group
                                expandedGroup = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    label = { Text("Select Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(text = cat) },
                            onClick = {
                                selectedCategory = cat
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Age",
                value = age,
                onValueChange = { age = it.filter { char -> char.isDigit() } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Phone Number",
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Re-Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = if (isLoading) "Creating Account..." else "Sign Up",
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                // Validate inputs
                if (name.isBlank() || email.isBlank() || gender.isBlank() ||
                    password.isBlank() || confirmPassword.isBlank() || age.isBlank() || phone.isBlank()) {
                    errorMessage = "Please fill in all fields"
                    return@PrimaryButton
                }

                val parsedAge = age.toIntOrNull()
                if (parsedAge == null || parsedAge <= 0 || parsedAge > 100) {
                    errorMessage = "Please enter a valid age (1–100)"
                    return@PrimaryButton
                }

                if (password != confirmPassword) {
                    errorMessage = "Passwords don't match"
                    return@PrimaryButton
                }

                if (password.length < 6) {
                    errorMessage = "Password should be at least 6 characters"
                    return@PrimaryButton
                }

                isLoading = true
                errorMessage = null

                scope.launch {
                    try {
                        // 1. Create auth user


                        // 2. Create user document in Firestore
                        val user = User(
                           // id = userId,
                            username = email,
                            fullName = name,
                            email = email,
                            role = selectedRole.lowercase(),
                            age = parsedAge,
                            gender = gender,
                            category = selectedCategory,
                            ageGroup = selectedGroup,
                            phone = phone,
                            password = password,
                            profileImageUrl = ""
                        )

                        val success = userService.createUser(user)

                        if (success) {
                            // Navigate to home on success
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Failed to save user data"
                            isLoading = false
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Registration failed"
                        isLoading = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = "Login",
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate("login")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}