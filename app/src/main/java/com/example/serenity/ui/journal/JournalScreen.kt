package com.example.serenity.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.serenity.data.model.JournalEntry
import com.example.serenity.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<JournalEntry?>(null) }

    val entries = viewModel.entries.collectAsState(initial = emptyList()).value

    val latestEntry = entries.firstOrNull()
    val latestMood = latestEntry?.aiSentiment ?: latestEntry?.manualMood ?: "Neutral"
    val latestScore = latestEntry?.sentimentScore ?: 0.0
    val hasLowMood = entries.take(3).any { (it.sentimentScore ?: 0.0) < -0.3 }

    val moodEmoji = when (latestMood) {
        "Positive" -> "😊"
        "Negative" -> "💙"
        else -> "🌿"
    }

    val moodMessage = when {
        entries.isEmpty() -> "Start writing to unlock your private mood insights."
        hasLowMood -> "You may have been feeling a little low recently. Be gentle with yourself."
        latestMood == "Positive" -> "Your recent entry looks positive. Keep noticing what helps you feel well."
        else -> "Your recent mood looks balanced. Keep checking in with yourself."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF7F3FF),
                        Color(0xFFEAF7FF),
                        Color.White
                    )
                )
            )
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "New Journal Entry",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = { showLogoutDialog = true }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Privacy",
                        tint = Color(0xFF7E77F2)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Private on-device journal",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your entries are stored locally on this device and analysed privately.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("How are you feeling today?") },
            placeholder = { Text("Write your thoughts here...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.addEntry(text, null)
                    text = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B83F6)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Entry")
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = moodEmoji,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Current Insight",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Latest mood: $latestMood • Score ${"%.2f".format(latestScore)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = moodMessage,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (hasLowMood) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Gentle prompt: consider taking a short break, breathing slowly, or speaking to someone you trust 💙",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A6FA5)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Previous Entries",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (entries.isEmpty()) {
            Text("No journal entries yet.")
        } else {
            LazyColumn {
                items(entries) { entry ->
                    val sentiment = entry.aiSentiment ?: entry.manualMood ?: "Neutral"
                    val emoji = when (sentiment) {
                        "Positive" -> "😊"
                        "Negative" -> "💙"
                        else -> "🌿"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = emoji)

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = sentiment,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete entry",
                                    tint = Color(0xFFE57373),
                                    modifier = Modifier
                                        .clickable { entryToDelete = entry }
                                        .padding(6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                                    .format(Date(entry.timestamp)),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = entry.text ?: "No text",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Sentiment score: ${"%.2f".format(entry.sentimentScore ?: 0.0)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }

    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        entryToDelete?.let { viewModel.deleteEntry(it) }
                        entryToDelete = null
                    }
                ) {
                    Text("Delete", color = Color(0xFFE57373))
                }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Entry") },
            text = { Text("Are you sure you want to delete this journal entry?") }
        )
    }
}