package com.example.serenity.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.serenity.data.model.JournalEntry

@Database(entities = [JournalEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}
