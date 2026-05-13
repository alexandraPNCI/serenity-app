package com.example.serenity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenity.data.model.JournalEntry
import com.example.serenity.data.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {

    private val _currentUserId = MutableStateFlow<Int?>(null)

    val entries: Flow<List<JournalEntry>> =
        _currentUserId.filterNotNull().flatMapLatest { userId ->
            repository.getEntriesForUser(userId)
        }

    fun setUser(userId: Int) {
        _currentUserId.value = userId
    }

    fun addEntry(text: String, selectedMood: String?) {
        val userId = _currentUserId.value ?: return

        viewModelScope.launch {
            val ai = if (text.isNotBlank()) {
                SentimentAnalyzer.analyze(text)
            } else {
                null
            }

            val newEntry = JournalEntry(
                text = text.ifBlank { null },
                manualMood = selectedMood,
                aiSentiment = ai?.label,
                sentimentScore = ai?.score,
                sentimentConfidence = ai?.confidence,
                timestamp = System.currentTimeMillis(),
                userId = userId
            )

            repository.insertEntry(newEntry)
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }

    fun generateSmartInsight(entries: List<JournalEntry>): String {

        if (entries.isEmpty()) {
            return "Start journaling to receive personalised insights."
        }

        val sortedEntries = entries.sortedBy { it.timestamp }
        val scores = sortedEntries.mapNotNull { it.sentimentScore }

        if (scores.isEmpty()) {
            return "Not enough emotional data yet."
        }

        val average = scores.average()

        val moodDirection = when {
            average > 0.25 -> "overall positive"
            average < -0.25 -> "slightly low"
            else -> "fairly balanced"
        }

        val recent = scores.takeLast(3).average()
        val previous = if (scores.size > 3) {
            scores.dropLast(3).average()
        } else {
            recent
        }

        val trend = when {
            recent > previous + 0.1 -> "an improving trend recently"
            recent < previous - 0.1 -> "a slight emotional decline recently"
            else -> "emotional stability over time"
        }

        var currentPositiveStreak = 0
        var longestPositiveStreak = 0

        scores.forEach {
            if (it > 0.25) {
                currentPositiveStreak++
                longestPositiveStreak = maxOf(longestPositiveStreak, currentPositiveStreak)
            } else {
                currentPositiveStreak = 0
            }
        }

        val positiveStreakText =
            if (longestPositiveStreak >= 3)
                " You experienced a $longestPositiveStreak-entry positive streak."
            else
                ""

        var currentLowStreak = 0
        var longestLowStreak = 0

        scores.forEach {
            if (it < -0.25) {
                currentLowStreak++
                longestLowStreak = maxOf(longestLowStreak, currentLowStreak)
            } else {
                currentLowStreak = 0
            }
        }

        val lowMoodWarning =
            if (longestLowStreak >= 3)
                " Serenity noticed several consecutive low mood entries."
            else
                ""

        val mean = average
        val variance = scores.map { (it - mean) * (it - mean) }.average()
        val volatility = kotlin.math.sqrt(variance)

        val stabilityText =
            if (volatility < 0.2)
                "Your emotions have been relatively stable."
            else
                "There have been noticeable emotional fluctuations."

        val encouragement = when {
            longestLowStreak >= 3 || average < -0.3 ->
                " It may help to take a break, practise self-care, or talk to someone you trust."

            volatility > 0.5 ->
                " Consider reflecting on what may be influencing these changes."

            else -> ""
        }

        return buildString {
            append("Your mood has been $moodDirection overall, with $trend. ")
            append(stabilityText)
            append(positiveStreakText)
            append(lowMoodWarning)
            append(encouragement)
        }
    }
}