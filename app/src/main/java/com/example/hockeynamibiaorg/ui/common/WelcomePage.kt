package com.example.hockeynamibiaorg.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hockeynamibiaorg.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController) {
    // Animation states
    var showLogo by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    // Create a staggered animation effect
    LaunchedEffect(key1 = true) {
        showLogo = true
        delay(300)
        showTitle = true
        delay(200)
        showSubtitle = true
        delay(300)
        showButtons = true
    }

    // Main background with a subtle gradient overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Optional: Add a subtle pattern or gradient overlay here

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // Logo with animation
            AnimatedVisibility(
                visible = showLogo,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow)) +
                        slideInVertically(
                            initialOffsetY = { -80 },
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
            ) {
                Card(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.hockey),
                            contentDescription = "Hockey Namibia Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title with animation
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
            ) {
                Text(
                    text = "Welcome to Hockey Namibia",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle with animation
            AnimatedVisibility(
                visible = showSubtitle,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 30 })
            ) {
                Text(
                    text = "The premier app for the best hockey organization in Namibia",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons with animation
            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 60 }),

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryButton(
                        text = "Let's Get Started",
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        navController.navigate("login")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SecondaryButton(
                        text = "Create Account",
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        navController.navigate("Register")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
