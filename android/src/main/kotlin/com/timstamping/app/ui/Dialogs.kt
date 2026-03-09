package com.timstamping.app.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.timstamping.app.Config
import com.timstamping.app.FileManager
import kotlinx.coroutines.delay

@Composable
fun FunctionCodeDialog(
    mode: String,
    config: Config?,
    fileManager: FileManager,
    onDismiss: () -> Unit,
    onCodeSubmitted: (code: String, message: String) -> Unit
) {
    var code by remember { mutableStateOf("") }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(24.dp)
                .width(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            val title = when (mode) {
                "in" -> "IN - CODE"
                "out" -> "OUT - CODE"
                else -> "FUNCTION - CODE"
            }
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Code Input
            TextField(
                value = code,
                onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) code = it },
                label = { Text("Enter Code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                readOnly = true
            )
            
            // Numeric Keypad
            NumericKeypad(
                onNumberClick = { digit ->
                    if (code.length < 4) {
                        code += digit
                        
                        if (code.length == 4) {
                            handleCodeSubmission(mode, code, config, fileManager, onCodeSubmitted, onDismiss)
                        }
                    }
                },
                onDeleteClick = {
                    if (code.isNotEmpty()) {
                        code = code.dropLast(1)
                    }
                },
                onCloseClick = onDismiss
            )
        }
    }
}

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("*", "0", "#")
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { button ->
                    when (button) {
                        "*" -> {
                            // X button (red, closes dialog)
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFFf44336), shape = RoundedCornerShape(8.dp))
                                    .clickable(onClick = onCloseClick),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("X", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                        "#" -> {
                            // Backspace button (left arrow)
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFF757575), shape = RoundedCornerShape(8.dp))
                                    .clickable(onClick = onDeleteClick),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("←", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                        else -> {
                            // Number button
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFF2196F3), shape = RoundedCornerShape(8.dp))
                                    .clickable { onNumberClick(button) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(button, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageCodeDialog(
    message: String,
    timeMessages: Long,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(timeMessages)
        onDismiss()
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(32.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun handleCodeSubmission(
    mode: String,
    code: String,
    config: Config?,
    fileManager: FileManager,
    onCodeSubmitted: (code: String, message: String) -> Unit,
    onDismiss: () -> Unit
) {
    when (mode) {
        "in", "out" -> {
            val dipendente = fileManager.getDipendenteByCode(code)
            if (dipendente == null) {
                // Wrong code
                onCodeSubmitted(code, "wrong code, please retry")
            } else {
                // Valid code - save stamping
                fileManager.saveStamping(code, if (mode == "in") "in" else "out")
                val endMessage = if (mode == "in") "START" else "END"
                onCodeSubmitted(code, "${dipendente.nome}\n$endMessage")
            }
        }
        "options" -> {
            val function = config?.let { fileManager.getSpecialCodeFunction(code, it) }
            when (function) {
                "version" -> {
                    val version = fileManager.getVersionFromConfig()
                    onCodeSubmitted(code, "VERSION: $version")
                }
                "download" -> {
                    onCodeSubmitted(code, "DOWNLOAD OK")
                }
                else -> {
                    onCodeSubmitted(code, "wrong code, please retry")
                }
            }
        }
    }
    onDismiss()
}
