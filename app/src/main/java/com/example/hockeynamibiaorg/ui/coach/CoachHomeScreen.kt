package com.example.hockeynamibiaorg.ui.coach

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hockeynamibiaorg.R
import com.example.hockeynamibiaorg.data.models.Coach
import com.example.hockeynamibiaorg.data.viewModels.CoachProfileViewModel
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel



data class NewsItem(val title: String, val description: String, val url: String)
data class MatchItem(val teams: String, val dateTime: String, val venue: String)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachHomeScreen(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    Scaffold(
        bottomBar = { CoachBottomNavigationBar(navController, currentRoute = currentRoute) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { CoachHeaderSection(navController) }
            item { CoachNewsUpdatesSection() }
            item { CoachUpcomingMatchSection() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachHeaderSection(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val userViewModel: UserViewModel = viewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val viewModel: CoachProfileViewModel = viewModel()

    // Replace this with your actual logic to get the coachId (e.g., from auth or session)
    val coachId = userViewModel.currentUser.value?.id

    // Load coach profile once on first composition
    LaunchedEffect(Unit) {
        if (coachId != null) {
            viewModel.loadCoachProfile(coachId)
        }
    }


    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        userViewModel.logout()
                        navController.navigate("logout") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Yes, Logout", color = DarkBlue)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel", color = DarkBlue)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF142143), Color(0xFF3F5291))
                )
            ) ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, GoldYellow),
                    color = Color.Transparent
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profileimage),
                        contentDescription = "Profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { navController.navigate("coach_profile") }
                    )
                }
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val coach = viewModel.coach
                if (coach != null) {
                    Text(
                        text ="Welcome Back ",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
                if (coach != null) {
                    Text(
                        text = "Coach ${coach.firstName} ${coach.lastName}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Search teams, players, or news...",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedBorderColor = GoldYellow,
                    cursorColor = GoldYellow
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun CoachNewsUpdatesSection() {
    val newsItems = listOf(
        NewsItem(
            "Team Selection Announcement",
            "Final team selection for the upcoming tournament will be announced next week.",
            "https://hockeynamibia.org/news/team-selection"
        ),
        NewsItem(
            "Training Schedule Update",
            "New training schedule effective from Monday. All players must attend.",
            "https://hockeynamibia.org/news/training-schedule"
        ),
        NewsItem(
            "Coaching Workshop",
            "Register for the advanced coaching workshop on 15th June 2025.",
            "https://hockeynamibia.org/news/coaching-workshop"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "News & Updates",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Text(
                text = "View All",
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Handle view all */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            newsItems.forEach { news ->
                CoachNewsCard(news)
            }
        }
    }
}

@Composable
fun CoachNewsCard(news: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldYellow.copy(alpha = 0.2f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "News",
                        tint = GoldYellow,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = news.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text(
                text = "Read More",
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CoachUpcomingMatchSection() {
    val matches = listOf(
        MatchItem(
            "Bank Windhoek Premier League: Your Team vs Coastal Hockey Club",
            "17/05/2025, 14:30",
            "Windhoek High School Hockey Fields"
        ),
        MatchItem(
            "Friendly Match: Your Team vs UNAM",
            "20/05/2025, 16:00",
            "UNAM Sports Field, Windhoek"
        ),
        MatchItem(
            "League Match: Your Team vs Wanderers",
            "27/05/2025, 15:00",
            "DTS Hockey Club, Olympia"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Matches",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
            Text(
                text = "View All",
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Handle view all */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            matches.forEach { match ->
                CoachMatchCard(match)
            }
        }
    }
}

@Composable
fun CoachMatchCard(match: MatchItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LighterBlue.copy(alpha = 0.2f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Match",
                        tint = LighterBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = match.teams,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = BlueAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.dateTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = BlueAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.venue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Match Details",
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CoachBottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = com.example.hockeynamibiaorg.ui.player.DarkBlue,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "coach_home",
            onClick = { navController.navigate("coach_home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                selectedTextColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                indicatorColor = com.example.hockeynamibiaorg.ui.player.DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Events") },
            label = { Text("Events") },
            selected = currentRoute == "coach_events",
            onClick = { navController.navigate("coach_events") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                selectedTextColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                indicatorColor = com.example.hockeynamibiaorg.ui.player.DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.group), contentDescription = "Coach Team",modifier= Modifier.size(30.dp)) },
            label = { Text("Teams") },
            selected = currentRoute == "teams",
            onClick = { navController.navigate("teams") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                selectedTextColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                indicatorColor = com.example.hockeynamibiaorg.ui.player.DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "coach_profile",
            onClick = { navController.navigate("coach_profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                selectedTextColor = com.example.hockeynamibiaorg.ui.player.GoldYellow,
                indicatorColor = com.example.hockeynamibiaorg.ui.player.DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )

        }
    }


