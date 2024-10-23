package com.example.androidtecapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar()
            MapViewWithMarkers()
            CollectionList()
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "Encuentra retos cerca de ti",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = false,
        singleLine = true
    )
}

@Composable
fun MapViewWithMarkers() {
    // TODO: Integrate Google Maps or any map library
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)) {
        Text(
            text = "MapView Placeholder",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun CollectionList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CollectionItem(
            title = "Recolecta en curso",
            address = "Av. Eugenio Garza Sada 2501 Sur, Tecnológico, 64849 Monterrey, N.L.",
            percentage = 80,
            isOngoing = true
        )
        CollectionItem(
            title = "Taller Programado",
            address = "Atlixcáyotl 5718, Reserva Territorial Atlixcáyotl, 72453 Puebla, Pue.",
            percentage = 50,
            isOngoing = false
        )
        // Add more items as needed
    }
}

@Composable
fun CollectionItem(title: String, address: String, percentage: Int, isOngoing: Boolean) {
    // State to track whether the item is expanded
    var isExpanded by remember { mutableStateOf(false) }

    // Main item container
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { isExpanded = !isExpanded } // Toggle expansion state on click
            .padding(16.dp)
    ) {
        // Row with title, address, and progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = address, fontSize = 14.sp, color = Color.Gray)
            }

            // Circular Progress with Percentage
            Box(
                modifier = Modifier
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = percentage / 100f,
                    color = if (isOngoing) Color.Green else Color.Red,
                    strokeWidth = 6.dp
                )
                Text(text = "$percentage%", fontSize = 12.sp)
            }
        }

        // Expanded content: only shown when isExpanded is true
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            // Add more details
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque odio justo, semper rutrum purus et, facilisis viverra lorem.",
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Materials received section
            Text(text = "Recibimos", fontWeight = FontWeight.Bold)
            Text(text = "  • Cartón\n  • Tetrapack\n  • Cajas\n  • Placas de Cartón", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Button to attend the event
            Button(
                onClick = { /* Handle attend click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text(text = "Asistir", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AndroidTecAppTheme {
        HomeScreen()
    }
}
