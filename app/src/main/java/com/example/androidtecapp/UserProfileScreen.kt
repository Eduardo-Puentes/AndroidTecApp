package com.example.androidtecapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

@Composable
fun UserProfileScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search Bar
            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            // User Information
            UserInfoSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Medals Section
            MedalsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Achievement List
            AchievementsList()
        }
    }
}

@Composable
fun UserInfoSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // QR Code Placeholder
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray)
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Add your QR code logic here
            Text(
                text = "QR Code",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // User Name
        Text(text = "Luis Isaias", fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
        Text(text = "Montes Rico", fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun MedalsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MedalItem(medalColor = Color(0xFFD4AF37), count = 38) // Gold
        MedalItem(medalColor = Color(0xFFADD8E6), count = 28) // Silver
        MedalItem(medalColor = Color(0xFFD4AF37), count = 42) // Gold
        MedalItem(medalColor = Color(0xFFE6BE8A), count = 4)  // Bronze
    }
}

@Composable
fun MedalItem(medalColor: Color, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(medalColor, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.brmedal), // Replace with your medal icon
                contentDescription = "Medal",
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }
        Text(text = "$count", fontSize = 14.sp)
    }
}

@Composable
fun AchievementsList() {
    Column {
        AchievementItem(description = "Entregaste 20.4kg de cartón", medalColor = Color(0xFFD4AF37))
        AchievementItem(description = "Gasto de agua reducido un 20%", medalColor = Color(0xFFADD8E6))
        AchievementItem(description = "28 Carpools este mes", medalColor = Color(0xFFE6BE8A))
        AchievementItem(description = "28 Carpools este mes", medalColor = Color(0xFFD4AF37))
    }
}

@Composable
fun AchievementItem(description: String, medalColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = description, fontSize = 16.sp)

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(medalColor, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.brmedal), // Replace with your medal icon
                contentDescription = "Medal",
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    AndroidTecAppTheme {
        UserProfileScreen()
    }
}
