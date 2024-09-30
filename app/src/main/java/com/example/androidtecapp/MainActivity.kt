package com.example.androidtecapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidtecapp.HomeScreen
import com.example.androidtecapp.LoginScreen
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTecAppTheme {
                MainScreenContent()
            }
        }
    }
}

@Composable
fun MainScreenContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // Track whether the user is logged in
        var isLoggedIn by remember { mutableStateOf(false) }
        var showLogin by remember { mutableStateOf(true) }

        Column(modifier = Modifier.padding(innerPadding)) {
            if (isLoggedIn) {
                // Render the MapScreen when the user is logged in
                HomeScreen()
            } else {
                // Toggle between login and registration screen
                if (showLogin) {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }, // User logs in successfully
                        onNavigateToRegister = { showLogin = false }
                    )
                } else {
                    RegisterScreen(onNavigateToLogin = { showLogin = true })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidTecAppTheme {
        MainScreenContent()
    }
}

