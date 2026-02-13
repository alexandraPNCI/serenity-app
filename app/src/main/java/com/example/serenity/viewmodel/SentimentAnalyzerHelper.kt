package com.example.serenity.viewmodel

object SentimentAnalyzer {

    fun analyze(text: String): Double {
        val positiveWords = listOf(
            "happy", "good", "great", "calm", "relaxed", "excited", "love", "hope"
        )

        val negativeWords = listOf(
            "sad", "bad", "angry", "anxious", "tired", "stress", "depressed", "lonely"
        )

        val words = text.lowercase().split(" ")

        var score = 0

        for (word in words) {
            if (positiveWords.contains(word)) score++
            if (negativeWords.contains(word)) score--
        }

        // Normalise score to -1.0 → 1.0
        return when {
            score > 0 -> 1.0
            score < 0 -> -1.0
            else -> 0.0
        }
    }
}

