package com.example.multithreadexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multithreadexample.ui.theme.MultiThreadExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiThreadExampleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MultiThreadApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiThreadApp() {
    Scaffold(
        topBar = { TopHeader() },
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                    )
                )
        ) {
            MultiThreadDemo(modifier = Modifier.fillMaxSize().padding(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Thread Runner", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color(0xFF1565C0)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
            label = { Text("Home", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
            label = { Text("Search", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White) },
            label = { Text("Alerts", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
            label = { Text("Profile", color = Color.White) }
        )
    }
}

@Composable
fun MultiThreadDemo(modifier: Modifier = Modifier) {
    var status by remember { mutableStateOf("Idle...") }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status + Progress Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Thread Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Spacer(Modifier.height(10.dp))
                Text(status, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)

                if (isRunning) {
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator(color = Color(0xFF1976D2))
                }
            }
        }

        // Action Button
        GradientButton(
            text = if (isRunning) "Running..." else "Start Task",
            enabled = !isRunning,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            isRunning = true
            scope.launch {
                withContext(Dispatchers.Default) {
                    for (i in 1..5) {
                        delay(1000)
                        withContext(Dispatchers.Main) { status = "Processing step $i / 5" }
                    }
                }
                withContext(Dispatchers.Main) {
                    status = "✅ Completed!"
                    isRunning = false
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val brush = Brush.horizontalGradient(listOf(Color(0xFF1565C0), Color(0xFF42A5F5)))
    Box(
        modifier = modifier
            .background(
                if (enabled) brush else Brush.horizontalGradient(listOf(Color.LightGray, Color.Gray)),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MultiThreadExampleTheme {
        MultiThreadApp()
    }
}
