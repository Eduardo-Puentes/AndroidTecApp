package com.example.androidtecapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtecapp.ui.theme.AndroidTecAppTheme
import androidx.compose.material3.Text
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.graphics.Bitmap
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.compose.runtime.Composable
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.compose.ui.graphics.Color as ComposeColor
import com.example.androidtecapp.models.getTaller
import com.example.androidtecapp.network.ApiService
import com.example.androidtecapp.network.RetrofitClient
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@Composable
fun HomeScreen() {
    var talleres by remember { mutableStateOf<List<getTaller>>(emptyList()) }
    var recolectas by remember { mutableStateOf<List<getRecolecta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val apiService = RetrofitClient.instance

    LaunchedEffect(Unit) {
        fetchData(
            onSuccess = { fetchedTalleres, fetchedRecolectas ->
                talleres = fetchedTalleres
                recolectas = fetchedRecolectas
                isLoading = false
            },
            onError = {
                Log.e("HomeScreen", "Failed to fetch data: $it")
                isLoading = false
            },
            apiService = apiService
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar()
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                MapViewWithCustomMarkers(talleres = talleres, recolectas = recolectas)
                CollectionList(talleres = talleres, recolectas = recolectas)
            }
        }
    }
}

@Composable
fun CollectionList(talleres: List<getTaller>, recolectas: List<getRecolecta>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Talleres Activos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        talleres.forEach { taller ->
            CollectionItem(
                title = taller.title,
                address = "${taller.longitude}, ${taller.latitude}",
                percentage = calculateCompletionPercentage(taller),
                isOngoing = isCourseOngoing(taller.startTime, taller.endTime)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recolectas Activas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        recolectas.forEach { recolecta ->
            CollectionItem(
                title = "Recolecta - ${recolecta.recollectID}",
                address = "${recolecta.longitude}, ${recolecta.latitude}",
                percentage = calculateRecolectaProgress(recolecta),
                isOngoing = isCourseOngoing(recolecta.startTime, recolecta.endTime)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun MapViewWithCustomMarkers(talleres: List<getTaller>, recolectas: List<getRecolecta>) {
    val context = LocalContext.current
    val initialLocation = LatLng(19.4326, -99.1332)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    val markers = (talleres.map { taller ->
        MarkerInfo(
            position = LatLng(taller.latitude, taller.longitude),
            percentage = calculateCompletionPercentage(taller)
        )
    } + recolectas.map { recolecta ->
        MarkerInfo(
            position = LatLng(recolecta.latitude, recolecta.longitude),
            percentage = calculateRecolectaProgress(recolecta)
        )
    })

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        markers.forEach { markerInfo ->
            val bitmap = createCustomMarkerBitmap(context, markerInfo.percentage)
            Marker(
                state = MarkerState(position = markerInfo.position),
                icon = BitmapDescriptorFactory.fromBitmap(bitmap),
                title = "Progress: ${markerInfo.percentage}%"
            )
        }
    }
}

data class getRecolecta(
    @SerializedName("RecollectID") val recollectID: String,
    @SerializedName("CollaboratorFBID") val collaboratorFBID: String,
    @SerializedName("Cardboard") val cardboard: Int,
    @SerializedName("Glass") val glass: Int,
    @SerializedName("Tetrapack") val tetrapack: Int,
    @SerializedName("Plastic") val plastic: Int,
    @SerializedName("Paper") val paper: Int,
    @SerializedName("Metal") val metal: Int,
    @SerializedName("StartTime") val startTime: Date,
    @SerializedName("EndTime") val endTime: Date,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Limit") val limit: Int,
    @SerializedName("DonationArray") val donationArray: List<Donation>
)

data class Donation(
    @SerializedName("UserFBID") val userFBID: String,
    @SerializedName("Username") val username: String,
    @SerializedName("Carboard") val cardboard: Double,
    @SerializedName("Glass") val glass: Double,
    @SerializedName("Metal") val metal: Double,
    @SerializedName("Paper") val paper: Double,
    @SerializedName("Plastic") val plastic: Double,
    @SerializedName("Tetrapack") val tetrapack: Double
)


fun calculateRecolectaProgress(recolecta: getRecolecta): Int {
    return 75
}

fun fetchData(
    onSuccess: (List<getTaller>, List<getRecolecta>) -> Unit,
    onError: (String) -> Unit,
    apiService: ApiService
) {
    val talleresCall = apiService.getAllCourses()
    val recolectasCall = apiService.getAllRecolectas()

    var talleres: List<getTaller>? = null
    var recolectas: List<getRecolecta>? = null

    talleresCall.enqueue(object : Callback<List<getTaller>> {
        override fun onResponse(call: Call<List<getTaller>>, response: Response<List<getTaller>>) {
            if (response.isSuccessful) {
                talleres = response.body() ?: emptyList()
                if (recolectas != null) {
                    onSuccess(talleres!!, recolectas!!)
                }
            } else {
                onError("Failed to fetch talleres")
            }
        }

        override fun onFailure(call: Call<List<getTaller>>, t: Throwable) {
            onError(t.message ?: "Unknown error")
        }
    })

    recolectasCall.enqueue(object : Callback<List<getRecolecta>> {
        override fun onResponse(call: Call<List<getRecolecta>>, response: Response<List<getRecolecta>>) {
            if (response.isSuccessful) {
                recolectas = response.body() ?: emptyList()
                if (talleres != null) {
                    onSuccess(talleres!!, recolectas!!)
                }
            } else {
                onError("Failed to fetch recolectas")
            }
        }

        override fun onFailure(call: Call<List<getRecolecta>>, t: Throwable) {
            onError(t.message ?: "Unknown error")
        }
    })
}



@Composable
fun SearchBar() {
    TextField(
        value = "Encuentra retos cerca de ti",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = false,
        singleLine = true
    )
}

data class MarkerInfo(
    val position: LatLng,
    val percentage: Int
)

@Composable
fun CollectionItem(title: String, address: String, percentage: Int, isOngoing: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDADADA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = address)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                CircularProgressIndicator(
                    progress = percentage / 100f,
                    color = if (isOngoing) Color.Green else Color.Gray,
                    strokeWidth = 6.dp
                )
                Text(
                    text = "$percentage%",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOngoing) Color.Green else Color.Gray
                )
            }
        }
    }
}

fun calculateCompletionPercentage(taller: getTaller): Int {
    return 50
}

fun isCourseOngoing(startTime: Date, endTime: Date): Boolean {
    val now = Date()
    return now.after(startTime) && now.before(endTime)
}

fun createCustomMarkerBitmap(context: Context, percentage: Int): Bitmap {
    val size = 100
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val backgroundPaint = Paint().apply {
        color = ComposeColor.Green.toArgb()
    }

    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, backgroundPaint)

    val progressPaint = Paint().apply {
        color = ComposeColor.Red.toArgb()
        style = Paint.Style.STROKE
        strokeWidth = 12f
        isAntiAlias = true
    }

    val rect = RectF(12f, 12f, size - 12f, size - 12f)
    val sweepAngle = percentage * 3.6f
    canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)

    val textPaint = Paint().apply {
        color = ComposeColor.White.toArgb()
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("$percentage%", radius, radius + textPaint.textSize / 3, textPaint)

    return bitmap
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AndroidTecAppTheme {
        HomeScreen()
    }
}
