package com.example.androidtecapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.androidtecapp.network.RetrofitClient
import com.example.androidtecapp.models.User
import com.example.androidtecapp.network.responses.UserResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFEA05), Color(0xFF5AC86E))
    )
    val noGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF5AC86E), Color(0xFF5AC86E))
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Crear Cuenta", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            GradientButton(
                text = "Crear Cuenta",
                onClick = {
                    registerUserWithFirebase(auth, context, username, email, password, onNavigateToLogin)
                },
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
                onClick = { /* Handle Google Sign-In if needed */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar con Google")
            }
        }
    }
}


fun registerUserWithFirebase(
    auth: FirebaseAuth,
    context: android.content.Context,
    username: String,
    email: String,
    password: String,
    onNavigateToLogin: () -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                firebaseUser?.let { user ->
                    val uid = user.uid

                    val newUser = User(FBID = uid, Username = username, Email = email)

                    registerUserInDatabase(newUser, context, onNavigateToLogin)
                }
            } else {
                Toast.makeText(context, "Firebase Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
}


fun registerUserInDatabase(user: User, context: android.content.Context, onNavigateToLogin: () -> Unit) {
    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val firebaseToken = task.result?.token

            if (firebaseToken != null) {
                RetrofitClient.instance.createUser(user).enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "User registered successfully in the database", Toast.LENGTH_LONG).show()
                            onNavigateToLogin()
                        } else {
                            Toast.makeText(context, "Failed to register user in the database", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.e("MyApp", "Network error occurred", t)

                        t.printStackTrace()

                        Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(context, "Unable to get Firebase token", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Error retrieving Firebase token", Toast.LENGTH_LONG).show()
        }
    }
}



