package com.example.hockeynamibiaorg.ui.common


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

import com.example.hockeynamibiaorg.R
import com.example.hockeynamibiaorg.data.viewModels.PlayerProfileViewModel
import com.example.hockeynamibiaorg.data.viewModels.UserViewModel
import com.example.hockeynamibiaorg.ui.theme.LighterBlue
import com.example.hockeynamibiaorg.ui.theme.DarkBlue
import com.example.hockeynamibiaorg.ui.theme.BlueAccent
import com.example.hockeynamibiaorg.ui.theme.GoldYellow



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCommon(navController: NavHostController) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item { HeaderSection(navController) }
            item { NewsUpdatesSection() }
            item { UpcomingMatchSection() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBlue, BlueAccent)
                )
            )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = { navController.navigate("login") },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldYellow)
                ) {
                    Text("Login", color = DarkBlue, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Welcome",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
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
                    cursorColor = GoldYellow,
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
@Composable
fun NewsUpdatesSection() {
    val newsItems = listOf(
        NewsItem(
            "National Team Selection",
            "Namibia Hockey Union announces national team selection for the upcoming Africa Cup. Applications close 30 May 2025.",
            "https://hockeynamibia.org/news/national-team-selection"
        ),
        NewsItem(
            "Annual General Meeting",
            "The Namibian Hockey Union will hold its AGM on 7 June 2025 at the Windhoek High School Hockey Fields.",
            "https://hockeynamibia.org/news/agm-2025"
        ),
        NewsItem(
            "Youth Development Program",
            "Registration open for the 2025 Youth Development Program targeting players aged 8-16 years across all regions.",
            "https://hockeynamibia.org/news/youth-program-2025"
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
                NewsCard(news)
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
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
fun UpcomingMatchSection() {
    val matches = listOf(
        MatchItem(
            "Bank Windhoek Premier League: Windhoek HC vs Coastal Hockey Club",
            "17/05/2025, 14:30",
            "Windhoek High School Hockey Fields"
        ),
        MatchItem(
            "Women's National League: UNAM vs Wanderers",
            "24/05/2025, 10:00",
            "UNAM Sports Field, Windhoek"
        ),
        MatchItem(
            "Youth League U16: Saints vs DTS",
            "31/05/2025, 09:00",
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
                MatchCard(match)
            }
        }
    }
}

@Composable
fun MatchCard(match: MatchItem) {
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
                        imageVector = Icons.Default.Home,
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
                text = "More Details",
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = DarkBlue,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "player_home",
            onClick = { navController.navigate("player_home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldYellow,
                selectedTextColor = GoldYellow,
                indicatorColor = DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Events") },
            label = { Text("Events") },
            selected = currentRoute == "player_events",
            onClick = { navController.navigate("player_events") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldYellow,
                selectedTextColor = GoldYellow,
                indicatorColor = DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.group), contentDescription = "Player Team",modifier= Modifier.size(28.dp))},
            label = { Text("Teams") },
            selected = currentRoute == "player_team",
            onClick = { navController.navigate("player_team") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldYellow,
                selectedTextColor = GoldYellow,
                indicatorColor = DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "player_profile",
            onClick = { navController.navigate("player_profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldYellow,
                selectedTextColor = GoldYellow,
                indicatorColor = DarkBlue,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            )
        )
    }
}

data class NewsItem(val title: String, val description: String, val url: String)
data class MatchItem(val teams: String, val dateTime: String, val venue: String)
