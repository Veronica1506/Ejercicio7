package com.example.ejercicio7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GameHubApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHubApp() {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F2027), Color(0xFF2C5364))
    )

    var search by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎮 GameHub", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFF1E1E2E),
                    titleContentColor = Color(0xFFBB86FC)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFFBB86FC),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Game")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            if (selectedGame == null) {
                Column {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("🔎 Buscar juegos", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFBB86FC),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    GameList(search) { gameClicked ->
                        selectedGame = gameClicked
                    }
                }
            } else {
                GameDetail(
                    game = selectedGame!!,
                    onBack = { selectedGame = null }
                )
            }
        }
    }
}

@Composable
fun GameList(search: String, onGameClick: (String) -> Unit) {
    val games = listOf("Minecraft", "Fortnite", "Zelda", "Among Us", "Mario Kart")
    val filteredGames = games.filter { it.contains(search, ignoreCase = true) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(filteredGames) { game ->
            GameCard(game, onClick = { onGameClick(game) })
        }
    }
}

@Composable
fun GameCard(game: String, onClick: () -> Unit) {
    var favorite by remember { mutableStateOf(false) }


    val scale by animateFloatAsState(if (favorite) 1.05f else 1f)
    val cardColor by animateColorAsState(
        if (favorite) Color(0xFF2E7D32) else Color(0xFF1E1E2E) // verde si es favorito
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .graphicsLayer(scaleX = scale, scaleY = scale),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Game Image",
                modifier = Modifier.size(70.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    game,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBB86FC)
                )
                Text(
                    "Juego popular",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }

            Checkbox(
                checked = favorite,
                onCheckedChange = { favorite = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFBB86FC),
                    uncheckedColor = Color.White
                )
            )
        }
    }
}

@Composable
fun GameDetail(game: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Game Detail Image",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(game, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Detalles del juego...", fontSize = 18.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = { onBack() }) {
            Text("⬅ Volver", fontSize = 18.sp)
        }
    }
}

