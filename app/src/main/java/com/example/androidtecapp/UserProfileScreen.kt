package com.example.androidtecapp

import android.util.Log
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.models.Notification
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
            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            UserInfoSection(userInfo = userInfo)

            Spacer(modifier = Modifier.height(16.dp))

            MedalsSection(mt = userInfo.MedalTrans, me = userInfo.MedalEnergy, mc = userInfo.MedalConsume, md = userInfo.MedalDesecho)

            Spacer(modifier = Modifier.
            height(16.dp))

            userInfo.NotificationArray?.let { AchievementsList(notifications = it) }
        }
    }
}

@Composable
fun UserInfoSection(userInfo: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val qrCodeText = userInfo.FBID
        val qrCodeBitmap = remember { generateQRCode(qrCodeText, 450) }

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (qrCodeBitmap != null) {
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Text(text = "QR Code Failed")
            }

            Image(
                painter = painterResource(id = R.drawable.qrframe),
                contentDescription = "QR Code Frame",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Text(text = userInfo.Username, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
    }
}


@Composable
fun MedalsSection(mt: Int, me: Int, mc: Int, md: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MedalItem(medalType = MedalType.GOLD, count = mt)
        MedalItem(medalType = MedalType.SILVER, count = me)
        MedalItem(medalType = MedalType.GOLD, count = mc)
        MedalItem(medalType = MedalType.BRONZE, count = md)
    }
}

@Composable
fun MedalItem(medalType: MedalType, count: Int) {
    val medalDrawable = when (medalType) {
        MedalType.GOLD -> R.drawable.brmedal
        MedalType.SILVER -> R.drawable.pumedal
        MedalType.BRONZE -> R.drawable.blmedal
        MedalType.PLATINUM -> R.drawable.yemedal
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = medalDrawable),
            contentDescription = "Medal",
            modifier = Modifier.size(40.dp),
            tint = Color.Unspecified
        )
        Text(text = "$count", fontSize = 14.sp)
    }
}

@Composable
fun AchievementsList(notifications: List<Notification>) {
    Column {
        notifications.forEach { notification ->
            val (description, medalType) = mapNotificationToAchievement(notification)
            Log.d("Medal", medalType.toString())
            AchievementItem(description = description, medalType = medalType)
        }
    }
}

@Composable
fun AchievementItem(description: String, medalType: MedalType) {
    val medalDrawable = when (medalType) {
        MedalType.GOLD -> R.drawable.yemedal
        MedalType.SILVER -> R.drawable.brmedal
        MedalType.BRONZE -> R.drawable.pumedal
        MedalType.PLATINUM -> R.drawable.blmedal
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
        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = medalDrawable),
                contentDescription = "Medal",
                modifier = Modifier.size(30.dp),
                alignment = Alignment.Center
            )
        }
    }
}


fun mapNotificationToAchievement(notification: Notification): Pair<String, MedalType> {
    return when (notification.NotificationType) {
        "RECOLLECT" -> Pair("Contribución en recolecta: ${notification.Message}", MedalType.BRONZE)
        "COURSE" -> Pair("Participaste en un taller: ${notification.Message}", MedalType.SILVER)
        "TRANSPORT" -> Pair("Realizaste una actividad de transporte: ${notification.Message}", MedalType.GOLD)
        else -> Pair("Logro desconocido: ${notification.Message}", MedalType.PLATINUM)
    }
}



@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    val sampleUser = User(
        Username = "Luis Isai",
        Email = "example@gmail.com",
        FBID = "sfsefse"
    )

    AndroidTecAppTheme {
        UserProfileScreen(userInfo = sampleUser)
    }
}


enum class MedalType {
    GOLD,
    SILVER,
    BRONZE,
    PLATINUM
}
