package com.example.androidtecapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
                HomeScreen() // Replace with your logged-in screen
            } else {
                if (showLogin) {
                    LoginScreen(
                        onLoginSuccess = { email, password ->
                            signInWithFirebase(email, password, auth, context) { success ->
                                if (success) {
                                    isLoggedIn = true
                                } else {
                                    // Show a Toast notification on error
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

fun signInWithFirebase(email: String, password: String, auth: FirebaseAuth, context: android.content.Context, onResult: (Boolean) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Log the user object when sign-in is successful
                val user = auth.currentUser
                Log.d("FirebaseAuth", "User signed in successfully: ${user?.uid}")

                // You can log other user properties as well
                Log.d("FirebaseAuth", "User email: ${user?.email}")
                Log.d("FirebaseAuth", "User display name: ${user?.displayName}")

                onResult(true)
            } else {
                // Log the error message
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
