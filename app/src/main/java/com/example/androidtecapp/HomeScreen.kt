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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.*
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.AbstractComposeView
import androidx.core.view.drawToBitmap
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.toArgb

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar()
            MapViewWithCustomMarkers()
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
fun MapViewWithCustomMarkers() {
    val context = LocalContext.current
    val initialLocation = LatLng(19.4326, -99.1332) // Example: Mexico City
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    // Example markers with percentages
    val markers = listOf(
        MarkerInfo(LatLng(19.4326, -99.1332), 50),
        MarkerInfo(LatLng(19.4426, -99.1332), 75),
        MarkerInfo(LatLng(19.4326, -99.1432), 90)
    )

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp), // Adjust height as needed
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        markers.forEach { markerInfo ->
            val bitmap = createCustomMarkerBitmap(context, markerInfo.percentage)
            Marker(
                state = MarkerState(position = markerInfo.position),
                icon = BitmapDescriptorFactory.fromBitmap(bitmap),
                title = "Progress: ${markerInfo.percentage}%"
            )
        }
    }
}

@Composable
fun CustomMarkerContent(percentage: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Transparent)
    ) {
        CircularProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier.size(100.dp),
            color = Color.Green,
            strokeWidth = 6.dp
        )
        Text(
            text = "$percentage%",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Red
        )
    }
}

// Data class to hold marker information
data class MarkerInfo(
    val position: LatLng,
    val percentage: Int
)

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

fun createCustomMarkerBitmap(context: Context, percentage: Int): Bitmap {
    val size = 100 // Size of the bitmap
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Background paint for circular marker
    val backgroundPaint = Paint().apply {
        color = ComposeColor.Green.toArgb() // Green color for marker background
        isAntiAlias = true
    }

    // Draw the circular background
    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, backgroundPaint)

    // Progress indicator paint
    val progressPaint = Paint().apply {
        color = ComposeColor.Red.toArgb() // Red color for progress bar
        style = Paint.Style.STROKE
        strokeWidth = 12f
        isAntiAlias = true
    }

    // Draw the progress arc
    val rect = RectF(12f, 12f, size - 12f, size - 12f)
    val sweepAngle = percentage * 3.6f // Convert percentage to degrees
    canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)

    // Text paint for percentage
    val textPaint = Paint().apply {
        color = ComposeColor.White.toArgb()
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("$percentage%", radius, radius + textPaint.textSize / 3, textPaint)

    return bitmap
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AndroidTecAppTheme {
        HomeScreen()
    }
}
