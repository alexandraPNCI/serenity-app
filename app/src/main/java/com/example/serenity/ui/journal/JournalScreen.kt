package com.example.serenity.ui.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serenity.viewmodel.JournalViewModel

@Composable
fun JournalScreen(viewModel: JournalViewModel) {

    var text by remember { mutableStateOf("") }
    val entries by viewModel.entries.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Journal Entry",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Write your thoughts...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.addEntry(text)
                    text = ""
                }
            }
        ) {
            Text("Save Entry")
        }   

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(entries) { entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = entry.text,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = when {
                            entry.sentimentScore > 0 -> "Mood: Positive"
                            entry.sentimentScore < 0 -> "Mood: Negative"
                            else -> "Mood: Neutral"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
