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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

@Composable
fun RankingScreen() {
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

            // Ranking Title
            Text(
                text = "Ranking Semanal",
                style = MaterialTheme.typography.headlineSmall,  // Updated for Material3
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ranking List
            RankingList()
        }
    }
}

@Composable
fun RankingList() {
    // Sample data
    val users = listOf(
        "Luis Isaias Montes Rico" to 53,
        "Sofía Martínez López" to 52,
        "Carlos Fernández Ruiz" to 50,
        "María Isabel Gómez Herrera" to 47,
        "Javier Ramírez Sánchez" to 42,
        "Lucía Navarro Torres" to 38,
        "Andrés Gutiérrez Pérez" to 37,
        "Gabriela Castillo Morales" to 33,
        "Fernando Rojas García" to 29,
        "Ana Patricia Ortega Jiménez" to 29
    )

    Column {
        users.forEachIndexed { index, user ->
            RankingItem(
                name = user.first,
                score = user.second,
                isTopUser = index == 0 // Highlight the top user
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RankingItem(name: String, score: Int, isTopUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isTopUser) Color(0xFFB8E994) else Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Name
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,  // Updated for Material3
            modifier = Modifier.weight(1f) // Occupy most of the space
        )

        // Score with medal
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFD4AF37), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$score",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge  // Updated for Material3
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    AndroidTecAppTheme {
        RankingScreen()
    }
}
