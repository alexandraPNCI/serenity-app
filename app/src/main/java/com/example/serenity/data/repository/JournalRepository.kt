package com.example.serenity.data.repository

import com.example.serenity.data.database.JournalDao
import com.example.serenity.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {

    fun getEntriesForUser(userId: Int): Flow<List<JournalEntry>> =
        journalDao.getEntriesForUser(userId)

    suspend fun insertEntry(entry: JournalEntry) =
        journalDao.insertEntry(entry)

    suspend fun deleteEntry(entry: JournalEntry) =
        journalDao.deleteEntry(entry)
}