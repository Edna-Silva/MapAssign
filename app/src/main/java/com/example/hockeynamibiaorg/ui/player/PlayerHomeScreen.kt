package com.example.hockeynamibiaorg.ui.player

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.hockeynamibiaorg.R
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerHomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection()
            NewsUpdatesSection()
            UpcomingMatchSection()
        }
    }
}


@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6CF48)) // Yellow background
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {}

        Text(
            text = "Player Home",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black, // Text color
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search teams, players, or news...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Surface(modifier = Modifier.size(60.dp).padding(5.dp), shape = CircleShape, border = BorderStroke(0.5.dp, Color.LightGray)){

                Image(painter = painterResource(id= R.drawable.profileimage),
                    modifier = Modifier.size(135.dp),
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop)
            }


        }
    }
}

@Composable
fun NewsUpdatesSection() {

}

@Composable
fun UpcomingMatchSection() {

}

@Composable
fun BottomNavigationBar() {

}