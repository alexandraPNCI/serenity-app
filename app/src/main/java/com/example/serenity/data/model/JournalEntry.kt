package com.example.serenity.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "journal_entries",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val text: String?,
    val manualMood: String?,
    val aiSentiment: String?,
    val sentimentScore: Double?,
    val sentimentConfidence: Double?,

    val timestamp: Long,
    val userId: Int
)