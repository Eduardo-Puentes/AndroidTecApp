package com.example.androidtecapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.models.User
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme

@Composable
fun UserProfileScreen(userInfo: User) {
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
            // Search Bar
            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            // User Information
            UserInfoSection(userInfo = userInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Medals Section
            MedalsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Achievement List
            AchievementsList()
        }
    }
}

@Composable
fun UserInfoSection(userInfo: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Generate and display the QR Code
        val qrCodeText = "FscKKoLCvzSxhVYn1G3ygv0uK9L2"
        val qrCodeBitmap = remember { generateQRCode(qrCodeText, 450) }

        Box(
            modifier = Modifier
                .size(180.dp) // Adjust size to fit the QR code and the frame
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // QR Code (This will be behind the frame)
            if (qrCodeBitmap != null) {
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize(), // Ensure QR code takes up the entire size
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Text(text = "QR Code Failed")
            }

            // Frame Image (This will be on top of the QR code)
            Image(
                painter = painterResource(id = R.drawable.qrframe), // Replace with your frame drawable
                contentDescription = "QR Code Frame",
                modifier = Modifier.fillMaxSize(), // Frame takes up full size
                contentScale = ContentScale.FillBounds // Ensure the frame fills the box
            )
        }

        // User Name
        Text(text = userInfo.Username, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
        Text(text = "Montes Rico", fontSize = 16.sp, color = Color.Gray)
    }
}


@Composable
fun MedalsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MedalItem(medalType = MedalType.GOLD, count = 38)
        MedalItem(medalType = MedalType.SILVER, count = 28)
        MedalItem(medalType = MedalType.GOLD, count = 42)
        MedalItem(medalType = MedalType.BRONZE, count = 4)
    }
}

@Composable
fun MedalItem(medalType: MedalType, count: Int) {
    val medalDrawable = when (medalType) {
        MedalType.GOLD -> R.drawable.brmedal // Replace with your gold medal drawable
        MedalType.SILVER -> R.drawable.pumedal // Replace with your silver medal drawable
        MedalType.BRONZE -> R.drawable.blmedal  // Replace with your bronze medal drawable
        MedalType.PLATINUM -> R.drawable.yemedal// Replace with your platinum medal drawable
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Image to display the medal based on its type
        Icon(
            painter = painterResource(id = medalDrawable), // Use the correct drawable
            contentDescription = "Medal",
            modifier = Modifier.size(40.dp),
            tint = Color.Unspecified
        )
        // Display the count of medals
        Text(text = "$count", fontSize = 14.sp)
    }
}

@Composable
fun AchievementsList() {
    Column {
        AchievementItem(description = "Entregaste 20.4kg de cartón", medalType = MedalType.GOLD)
        AchievementItem(description = "Gasto de agua reducido un 20%", medalType = MedalType.SILVER)
        AchievementItem(description = "28 Carpools este mes", medalType = MedalType.BRONZE)
        AchievementItem(description = "28 Carpools este mes", medalType = MedalType.GOLD)
    }
}

@Composable
fun AchievementItem(description: String, medalType: MedalType) {
    val medalDrawable = when (medalType) {
        MedalType.GOLD -> R.drawable.yemedal // Replace with your gold medal drawable
        MedalType.SILVER -> R.drawable.brmedal // Replace with your silver medal drawable
        MedalType.BRONZE -> R.drawable.pumedal // Replace with your bronze medal drawable
        MedalType.PLATINUM -> R.drawable.blmedal // Replace with your platinum medal drawable
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Description text
        Text(text = description, fontSize = 16.sp)

        // Medal Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = medalDrawable), // Use the correct drawable for each medal type
                contentDescription = "Medal",
                modifier = Modifier.size(30.dp),
                tint = Color.Unspecified // Prevent tinting to keep the original medal colors
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    // Create a sample user data for preview
    val sampleUser = User(
        Username = "Luis Isaia",
        Email = "example@gmail.com"
        // Add other fields if needed
    )

    AndroidTecAppTheme {
        UserProfileScreen(userInfo = sampleUser) // Pass the sample user to the preview
    }
}


enum class MedalType {
    GOLD,
    SILVER,
    BRONZE,
    PLATINUM // Example of a fourth type
}
