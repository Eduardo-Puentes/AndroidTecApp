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
import com.example.androidgreenmatescolab.LoginScreen
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
        var showLogin by remember { mutableStateOf(true) }

        Column(modifier = Modifier.padding(innerPadding)) {
            if (showLogin) {
                LoginScreen(onNavigateToRegister = { showLogin = false })
            } else {
                RegisterScreen(onNavigateToLogin = { showLogin = true })
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