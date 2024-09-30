package com.example.androidtecapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.androidtecapp.GradientButton
import com.example.androidtecapp.R

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFEA05), Color(0xFF5AC86E)) // Example gradient from light green to dark green
    )
    val noGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF5AC86E), Color(0xFF5AC86E)) // Example gradient from light green to dark green
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background images
        Image(
            painter = painterResource(id = R.drawable.top_background_image), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(200.dp) // Adjust the height as needed
        )

        Image(
            painter = painterResource(id = R.drawable.bottom_background_image), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(200.dp) // Adjust the height as needed
        )

        // Add Spacer to position the logo and title lower down on the screen
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp)) // Adjust this height to move the logo down

            // Add logo image
            Image(
                painter = painterResource(id = R.drawable.greenmateslogo), // Replace with your logo image resource
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp) // Adjust size as needed
            )

            // Add title below the logo
            Text(
                text = "GreenMates", // Your title
                style = MaterialTheme.typography.titleLarge, // Use a title style
                modifier = Modifier.padding(top = 0.dp) // Add padding to create space between logo and title
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.Center), // Center the content
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Crear Cuenta", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "", // Aquí deberías manejar el estado
                onValueChange = { /* Manejar cambio */ },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = "", // Aquí deberías manejar el estado
                onValueChange = { /* Manejar cambio */ },
                label = { Text("Número Telefónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = "", // Aquí deberías manejar el estado
                onValueChange = { /* Manejar cambio */ },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            GradientButton(
                text = "Crear Cuenta",
                onClick = { /* Handle registration */ },
                gradient = gradient
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "o", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(20.dp))

            GradientButton(
                text = "Iniciar Sesión",
                onClick = { onNavigateToLogin() },
                gradient = noGradient
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Manejar clic para iniciar sesión con Google */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar con Google")
            }
        }
    }
}
