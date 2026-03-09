package com.timstamping.app.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timstamping.app.Config
import com.timstamping.app.FileManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimestampingApp(context: Context) {
    val fileManager = remember { FileManager(context) }
    val config = remember { mutableStateOf<Config?>(null) }
    val currentTime = remember { mutableStateOf("") }
    val currentDate = remember { mutableStateOf("") }
    
    var showFunctionCodeDialog by remember { mutableStateOf(false) }
    var showMsgCodeDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf("") } // "in", "out", "options"
    var msgCodeMessage by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        config.value = fileManager.loadConfig()
        
        // Update time and date every second
        while (true) {
            currentTime.value = fileManager.getCurrentTime()
            currentDate.value = fileManager.getCurrentDateFormatted()
            delay(1000)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header Section
        HeaderSection(config.value)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Time and Date Section
        TimeAndDateSection(currentTime.value, currentDate.value)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Buttons Section
        ButtonsSection(
            onInPressed = {
                dialogMode = "in"
                showFunctionCodeDialog = true
            },
            onOutPressed = {
                dialogMode = "out"
                showFunctionCodeDialog = true
            },
            onOptionsPressed = {
                dialogMode = "options"
                showFunctionCodeDialog = true
            }
        )
    }
    
    // Function Code Dialog
    if (showFunctionCodeDialog) {
        FunctionCodeDialog(
            mode = dialogMode,
            config = config.value,
            fileManager = fileManager,
            onDismiss = { showFunctionCodeDialog = false },
            onCodeSubmitted = { code, message ->
                showFunctionCodeDialog = false
                msgCodeMessage = message
                showMsgCodeDialog = true
            }
        )
    }
    
    // Message Code Dialog
    if (showMsgCodeDialog) {
        MessageCodeDialog(
            message = msgCodeMessage,
            timeMessages = config.value?.timeMessages ?: 3000L,
            onDismiss = { showMsgCodeDialog = false }
        )
    }
}

@Composable
fun HeaderSection(config: Config?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1976D2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = config?.title ?: "Time Stampings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = config?.subTitle ?: "",
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TimeAndDateSection(time: String, date: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            textAlign = TextAlign.Center
        )
        Text(
            text = date,
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ButtonsSection(
    onInPressed: () -> Unit,
    onOutPressed: () -> Unit,
    onOptionsPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // IN Button (Left)
        CircleButton(
            text = "IN",
            backgroundColor = Color(0xFF4CAF50),
            size = 120.dp,
            onClick = onInPressed
        )
        
        // Options Button (Center)
        RectangleButton(
            text = "OPTIONS",
            onClick = onOptionsPressed
        )
        
        // OUT Button (Right)
        CircleButton(
            text = "OUT",
            backgroundColor = Color(0xFFf44336),
            size = 120.dp,
            onClick = onOutPressed
        )
    }
}
