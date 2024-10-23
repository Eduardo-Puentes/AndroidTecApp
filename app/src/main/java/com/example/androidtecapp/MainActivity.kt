package com.example.androidtecapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            AndroidTecAppTheme {
                MainScreenContent(auth)
            }
        }
    }
}

@Composable
fun MainScreenContent(auth: FirebaseAuth) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        var isLoggedIn by remember { mutableStateOf(false) }
        var showLogin by remember { mutableStateOf(true) }
        val context = LocalContext.current // Context needed for Toast

        Column(modifier = Modifier.padding(innerPadding)) {
            if (isLoggedIn) {
                // Handle the main screen content after login
                MainAppWithBottomNav()
            } else {
                if (showLogin) {
                    LoginScreen(
                        onLoginSuccess = { email, password ->
                            signInWithFirebase(email, password, auth) { success ->
                                if (success) {
                                    isLoggedIn = true
                                } else {
                                    Toast.makeText(context, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onNavigateToRegister = { showLogin = false }
                    )
                } else {
                    RegisterScreen(onNavigateToLogin = { showLogin = true })
                }
            }
        }
    }
}

@Composable
fun MainAppWithBottomNav() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { selectedScreen = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                Screen.Home -> HomeScreen()
                Screen.UserProfile -> UserProfileScreen()
                // Add more screens here as necessary
                //Screen.Groups -> TODO()
                Screen.Ranking -> RankingScreen()
                //Screen.Search -> TODO()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFE0F2F1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // User Icon
            IconButton(onClick = { onScreenSelected(Screen.UserProfile) }) {
                Icon(
                    painter = painterResource(id = R.drawable.person_icon),
                    contentDescription = "User",
                    modifier = Modifier.size(20.dp),
                    tint = if (selectedScreen == Screen.UserProfile) Color.Green else Color.Gray
                )
            }

            // Groups Icon
//            IconButton(onClick = { onScreenSelected(Screen.Groups) }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.people_icon),
//                    contentDescription = "Groups",
//                    modifier = Modifier.size(20.dp),
//                    tint = if (selectedScreen == Screen.Groups) Color.Green else Color.Gray
//                )
//            }

            // Logo Icon (Home)
            IconButton(onClick = { onScreenSelected(Screen.Home) }) {
                Icon(
                    painter = painterResource(id = R.drawable.greenmateslogo),
                    contentDescription = "Home",
                    tint = if (selectedScreen == Screen.Home) Color.Green else Color.Gray
                )
            }

            // Ranking Icon
            IconButton(onClick = { onScreenSelected(Screen.Ranking) }) {
                Icon(
                    painter = painterResource(id = R.drawable.list_icon),
                    contentDescription = "Ranking",
                    modifier = Modifier.size(20.dp),
                    tint = if (selectedScreen == Screen.Ranking) Color.Green else Color.Gray
                )
            }

//            // Search Icon
//            IconButton(onClick = { onScreenSelected(Screen.Search) }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.search_icon),
//                    contentDescription = "Search",
//                    modifier = Modifier.size(20.dp),
//                    tint = if (selectedScreen == Screen.Search) Color.Green else Color.Gray
//                )
//            }
        }
    }
}

fun signInWithFirebase(
    email: String,
    password: String,
    auth: FirebaseAuth,
    onResult: (Boolean) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("FirebaseAuth", "User signed in successfully: ${user?.uid}")
                Log.d("FirebaseAuth", "User email: ${user?.email}")
                Log.d("FirebaseAuth", "User display name: ${user?.displayName}")
                onResult(true)
            } else {
                Log.e("FirebaseAuth", "Sign-in failed: ${task.exception?.localizedMessage}")
                onResult(false)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidTecAppTheme {
        MainScreenContent(FirebaseAuth.getInstance()) // For previewing
    }
}

enum class Screen {
    Home,
    UserProfile,
    //Groups,
    Ranking,
    //Search
}
