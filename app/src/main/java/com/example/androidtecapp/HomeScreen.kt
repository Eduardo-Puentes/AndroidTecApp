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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color as ComposeColor
import com.example.androidtecapp.models.getTaller
import com.example.androidtecapp.network.ApiService
import com.example.androidtecapp.network.RetrofitClient
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen() {
    var talleres by remember { mutableStateOf<List<getTaller>>(emptyList()) }
    var recolectas by remember { mutableStateOf<List<getRecolecta>>(emptyList()) }
    var filteredTalleres by remember { mutableStateOf<List<getTaller>>(emptyList()) }
    var filteredRecolectas by remember { mutableStateOf<List<getRecolecta>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
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

    LaunchedEffect(searchQuery) {
        filteredTalleres = talleres.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
        filteredRecolectas = recolectas.filter {
            it.endTime.toString().contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if(!(filteredTalleres.isEmpty() && filteredRecolectas.isEmpty())) {
                    MapViewWithCustomMarkers(talleres = filteredTalleres, recolectas = filteredRecolectas)
                    CollectionList(talleres = filteredTalleres, recolectas = filteredRecolectas)
                }
                else {
                    MapViewWithCustomMarkers(talleres = talleres, recolectas = recolectas)
                    CollectionList(talleres = talleres, recolectas = recolectas)
                }
            }
        }
    }
}

@Composable
fun CollectionList(talleres: List<getTaller>, recolectas: List<getRecolecta>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Talleres Activos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(talleres) { taller ->
            CollectionItem(
                title = taller.title,
                address = "${taller.longitude}, ${taller.latitude}",
                percentage = calculateCompletionPercentage(taller),
                extraInfo = CollectionExtraInfo(
                    pillar = taller.pillar,
                    startTime = taller.startTime.formattedDate(),
                    endTime = taller.endTime.formattedDate(),
                    collaboratorFBID = taller.collaboratorFBID,
                    limit = taller.limit
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recolectas Activas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(recolectas) { recolecta ->
            CollectionItem(
                title = "Recolecta - ${recolecta.recollectID}",
                address = "${recolecta.longitude}, ${recolecta.latitude}",
                percentage = calculateRecolectaProgress(recolecta),
                extraInfo = CollectionExtraInfo(
                    pillar = null,
                    startTime = recolecta.startTime.formattedDate(),
                    endTime = recolecta.endTime.formattedDate(),
                    collaboratorFBID = recolecta.collaboratorFBID,
                    limit = recolecta.limit
                )
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
    return if (recolecta.limit > 0) {
        recolecta.donationArray.count() * 100 / recolecta.limit
    }
    else {
        100
    }
}
fun calculateCompletionPercentage(taller: getTaller): Int {
    return if (taller.limit > 0) {
        taller.assistantArray.count() * 100 / taller.limit
    }
    else {
        100
    }
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
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Encuentra retos cerca de ti") },
        singleLine = true
    )
}

data class MarkerInfo(
    val position: LatLng,
    val percentage: Int
)

@Composable
fun CollectionItem(
    title: String,
    address: String,
    percentage: Int,
    extraInfo: CollectionExtraInfo
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDADADA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
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
                        color = Color.Green,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "$percentage%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.padding(vertical = 4.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            Color(0xFFDADADA),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    extraInfo.pillar?.let {
                        Text("Pillar: $it", fontSize = 14.sp)
                    }
                    Text("Start: ${extraInfo.startTime}", fontSize = 14.sp)
                    Text("End: ${extraInfo.endTime}", fontSize = 14.sp)
                    Text("Collaborator: ${extraInfo.collaboratorFBID}", fontSize = 14.sp)
                    Text("Limit: ${extraInfo.limit}", fontSize = 14.sp)
                }
            }
        }
    }
}

data class CollectionExtraInfo(
    val pillar: String?,
    val startTime: String,
    val endTime: String,
    val collaboratorFBID: String,
    val limit: Int
)

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

fun Date.formattedDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(this)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AndroidTecAppTheme {
        HomeScreen()
    }
}
