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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.androidtecapp.network.RetrofitClient
import com.example.androidtecapp.models.User
import com.example.androidtecapp.network.responses.UserResponse

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
    var isLoggedIn by remember { mutableStateOf(false) }
    var userInfo by remember { mutableStateOf<User?>(null) } // Store user info here
    var showLogin by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (isLoggedIn && userInfo != null) {
                // Pass the user info to other screens once the user is logged in
                MainAppWithBottomNav(userInfo = userInfo!!)
            } else {
                if (showLogin) {
                    LoginScreen(
                        onLoginSuccess = { email, password ->
                            signInWithFirebase(email, password, auth) { success, fetchedUserInfo ->
                                if (success && fetchedUserInfo != null) {
                                    isLoggedIn = true
                                    userInfo = fetchedUserInfo // Store fetched user info
                                } else {
                                    Log.e("Res","$fetchedUserInfo");
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
fun MainAppWithBottomNav(userInfo: User) {
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
                Screen.UserProfile -> UserProfileScreen(userInfo = userInfo) // Pass user info here
                Screen.Ranking -> RankingScreen()
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
    onResult: (Boolean, User?) -> Unit
) {
    Log.d("SignIn", "Attempting to sign in with email: $email and password: $password")

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("SignIn", "Firebase sign-in successful")

                val firebaseUser = auth.currentUser
                firebaseUser?.let { user ->
                    val email = user.email ?: ""
                    val username = user.displayName ?: ""

                    // Creating a User object based on Firebase information
                    val userInfo = User(Username = username, Email = email)

                    Log.d("SignIn", "Firebase User Email: $email, Username: $username")

                    // Returning the Firebase user info directly
                    onResult(true, userInfo)
                }
            } else {
                Log.e("SignIn", "Firebase sign-in failed: ${task.exception?.message}")
                onResult(false, null)
            }
        }
}





// Fetch user data from your API using the Firebase UID
fun fetchUserData(uid: String, onResult: (User?) -> Unit) {
    Log.d("FetchUserData", "Fetching user data for UID: $uid")

    // Call the external API to get the user data
    RetrofitClient.instance.getUser(uid).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if (response.isSuccessful) {
                Log.d("FetchUserData", "Successfully retrieved user data")
                onResult(response.body()?.user) // Pass the User object to the callback
            } else {
                Log.e("FetchUserData", "Failed to retrieve user data, response code: ${response.code()}")
                onResult(null)
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.e("FetchUserData", "Network error while fetching user data: ${t.message}")
            onResult(null)
        }
    })
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
