package com.example.androidtecapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

@Composable
fun HomeScreen() {
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
            percentage = 100,
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
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)  // Background for the Row
            .padding(8.dp),  // Padding after background to avoid clipping content
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)  // Space between text and progress indicator
        ) {
            Text(text = title, fontSize = 18.sp)
            Text(text = address, fontSize = 14.sp)
        }
        Box(
            modifier = Modifier
                .size(50.dp)  // Ensure the CircularProgressIndicator has a fixed size
                .align(Alignment.CenterVertically),  // Align the Box with the text
            contentAlignment = Alignment.Center  // Center all content within the Box
        ) {
            CircularProgressIndicator(
                progress = percentage / 100f,  // Ensure percentage is a float
                color = if (isOngoing) Color.Green else Color.Red,
                strokeWidth = 6.dp,
                trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
            )
            Text(
                text = "$percentage%",
                fontSize = 12.sp,  // Adjust the text size to fit the circle
                color = Color.Black,  // Text color
                modifier = Modifier.padding(2.dp)  // Fine-tuning the padding for centering
            )
        }
    }
}


@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFE0F2F1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // This distributes the items evenly with space around them
        ) {
            IconButton(onClick = { /* Handle user icon click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.person_icon),
                    contentDescription = "User",
                    modifier = Modifier.size(20.dp)  // Set the size of the icon to 32.dp or any size you prefer
                )
            }
            IconButton(onClick = { /* Handle groups icon click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.people_icon),
                    contentDescription = "Groups",
                    modifier = Modifier.size(20.dp)  // Adjust the size as needed
                )
            }
            IconButton(onClick = { /* Handle logo icon click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.greenmateslogo),
                    contentDescription = "Logo",
                )
            }
            IconButton(onClick = { /* Handle list icon click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.list_icon),
                    contentDescription = "Ranking",
                    modifier = Modifier.size(20.dp)  // Adjust the size as needed
                )
            }
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)  // Adjust the size as needed
                )
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