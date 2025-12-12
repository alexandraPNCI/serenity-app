package com.example.serenity.viewmodel

object SentimentAnalyzerHelper {

    private val positiveWords = listOf(
        "happy", "great", "good", "amazing", "love", "excited",
        "calm", "relaxed", "content", "proud", "joy", "wonderful"
    )

    private val negativeWords = listOf(
        "sad", "bad", "upset", "angry", "anxious", "depressed",
        "worried", "stressed", "tired", "frustrated", "hate",
        "terrible"
    )

    fun analyze(text: String): Float {
        val lower = text.lowercase()

        var score = 0f

        for (word in positiveWords) {
            if (lower.contains(word)) score += 1
        }
        for (word in negativeWords) {
            if (lower.contains(word)) score -= 1
        }

        // Normalize score to 0–1 range
        return ((score + 5) / 10).coerceIn(0f, 1f)
    }
}

