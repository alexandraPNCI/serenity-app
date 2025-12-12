package com.example.serenity.ui.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Journal Entry",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("Write your thoughts...") },
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(onClick = {
            viewModel.addEntry(text)
            text = ""
        }) {
            Text("Save Entry")
        }
    }
}
