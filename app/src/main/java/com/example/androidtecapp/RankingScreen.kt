package com.example.androidtecapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.network.RetrofitClient
import com.example.androidtecapp.network.TopTenArray
import com.example.androidtecapp.network.TopTenUser
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RankingScreen() {
    var topTenUsers by remember { mutableStateOf<List<TopTenUser>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchTopTenUsers(
            onSuccess = { fetchedUsers ->
                topTenUsers = fetchedUsers
                isLoading = false
            },
            onError = { error ->
                errorMessage = error
                isLoading = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ranking Semanal",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text(
                    text = "Error: $errorMessage",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
                else -> RankingList(users = topTenUsers)
            }
        }
    }
}

@Composable
fun RankingList(users: List<TopTenUser>) {
    Column {
        users.forEach { user ->
            RankingItem(
                name = user.Username,
                score = user.Place,
                isTopUser = user.Place == 1
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RankingItem(name: String, score: Int, isTopUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isTopUser) Color(0xFFB8E994) else Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFD4AF37), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$score",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


fun fetchTopTenUsers(
    onSuccess: (List<TopTenUser>) -> Unit,
    onError: (String) -> Unit
) {
    val apiService = RetrofitClient.instance
    apiService.getTopTen().enqueue(object : Callback<TopTenArray> {
        override fun onResponse(call: Call<TopTenArray>, response: Response<TopTenArray>) {
            if (response.isSuccessful) {
                val topTenArray = response.body()
                onSuccess(topTenArray?.UserArray ?: emptyList())
            } else {
                onError("Failed to fetch ranking data")
            }
        }

        override fun onFailure(call: Call<TopTenArray>, t: Throwable) {
            onError(t.message ?: "Unknown error")
        }
    })
}
