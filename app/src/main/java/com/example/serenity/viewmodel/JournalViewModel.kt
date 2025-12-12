package com.example.serenity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenity.data.model.JournalEntry
import com.example.serenity.data.repository.JournalRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {

    val entries: Flow<List<JournalEntry>> = repository.getAllEntries()

    fun addEntry(text: String) {
        viewModelScope.launch {
            val newEntry = JournalEntry(
                text = text,
                sentimentScore = 0.0,
                timestamp = Date().time
            )
            repository.insertEntry(newEntry)
        }
    }
}
