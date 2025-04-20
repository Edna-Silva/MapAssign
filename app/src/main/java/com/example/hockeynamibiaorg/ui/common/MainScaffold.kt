package com.example.hockeynamibiaorg.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text


@Composable
fun HockeyScaffold(
    title: String,
    navController: NavController,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    profileImage: Painter? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            HockeyTopAppBar(
                title = title,
                onNotificationClick = onNotificationClick,
                onProfileClick = onProfileClick,
                profileImage = profileImage
            )
        },
        bottomBar = {
            if (currentRoute in listOf(
                    Navigation.Home.route,
                    Navigation.Teams.route,
                    Navigation.Players.route,
                    Navigation.Events.route
                )) {
                HockeyBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
// ... (Keep your existing HockeyBottomBar implementation)

@Preview(showBackground = true)
@Composable
fun PreviewBottomBar() {
    val fakeNavController = rememberNavController()
    HockeyBottomBar(
        navController = fakeNavController,
        currentRoute = Navigation.Home.route // Force a route to show the bar
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFullScaffold() {
    val fakeNavController = rememberNavController()

    // Simulate a navigation state where BottomBar should appear
    HockeyScaffold(
        title = "Coach Dashboard",
        navController = fakeNavController,
        onNotificationClick = { /* Handle notification click */ },
        onProfileClick = { /* Handle profile click */ },
        profileImage = null // or use painterResource(R.drawable.placeholder)
    ) { innerPadding ->
        Text("Screen Content", modifier = Modifier.padding(innerPadding))
    }
}
