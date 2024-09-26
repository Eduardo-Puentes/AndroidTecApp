package com.example.androidgreenmatescolab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.R

@Composable
fun MapScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar() }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = title, fontSize = 18.sp)
            Text(text = address, fontSize = 14.sp)
        }
        CircularProgressIndicator(
            progress = percentage / 100f,
            color = if (isOngoing) Color.Green else Color.Red,
            strokeWidth = 6.dp,
            modifier = Modifier.size(50.dp)
        )
        Text(text = "$percentage%", modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFE0F2F1)
    ) {
        IconButton(onClick = { /* Handle user icon click */ }) {
            Icon(painter = painterResource(id = R.drawable.greenmateslogo), contentDescription = "User")
        }
        IconButton(onClick = { /* Handle groups icon click */ }) {
            Icon(painter = painterResource(id = R.drawable.greenmateslogo), contentDescription = "Groups")
        }
        Spacer(modifier = Modifier.weight(1f)) // Spacer to push the next icons to the right
        IconButton(onClick = { /* Handle leaf icon click */ }) {
            Icon(painter = painterResource(id = R.drawable.greenmateslogo), contentDescription = "Leaf")
        }
        IconButton(onClick = { /* Handle menu icon click */ }) {
            Icon(painter = painterResource(id = R.drawable.greenmateslogo), contentDescription = "Menu")
        }
    }
}
