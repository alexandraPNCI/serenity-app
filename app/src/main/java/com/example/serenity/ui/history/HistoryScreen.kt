package com.example.serenity.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serenity.viewmodel.JournalViewModel

@Composable
fun HistoryScreen(viewModel: JournalViewModel) {

    val entries = viewModel.entries.collectAsState(initial = emptyList()).value

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(entries) { entry ->
            Column(modifier = Modifier.padding(12.dp)) {
                Text(entry.text, style = MaterialTheme.typography.bodyLarge)
                Text("Sentiment: ${entry.sentimentScore}")
            }
        }
    }
}
