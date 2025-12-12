package com.example.serenity.data.repository

import com.example.serenity.data.database.JournalDao
import com.example.serenity.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val dao: JournalDao) {

    fun getAllEntries(): Flow<List<JournalEntry>> {
        return dao.getAllEntries()
    }

    suspend fun insertEntry(entry: JournalEntry) {
        dao.insertEntry(entry)
    }
}
