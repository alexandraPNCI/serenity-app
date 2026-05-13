package com.example.serenity.viewmodel

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object SentimentAnalyzer {

    private val lexicon: Map<String, Double> = mapOf(
        // Strong positive
        "happy" to 3.0, "proud" to 3.0, "amazing" to 3.2, "excellent" to 3.0,
        "great" to 2.8, "love" to 3.0, "grateful" to 2.8, "excited" to 2.6,
        "peaceful" to 2.4, "calm" to 2.2, "relaxed" to 2.2, "safe" to 2.0,
        "hopeful" to 2.2, "better" to 2.0, "good" to 2.0, "content" to 2.0,
        "confident" to 2.4, "motivated" to 2.3, "positive" to 2.2,

        // Strong negative
        "sad" to -3.0, "depressed" to -3.5, "anxious" to -3.0, "angry" to -3.0,
        "stressed" to -3.0, "stress" to -2.8, "overwhelmed" to -3.2,
        "lonely" to -3.0, "panic" to -3.2, "scared" to -2.8, "worried" to -2.6,
        "hurt" to -2.6, "exhausted" to -2.8, "tired" to -2.0, "bad" to -2.2,
        "upset" to -2.6, "low" to -2.4, "crying" to -3.0, "hopeless" to -3.4,
        "negative" to -2.2, "awful" to -3.2, "terrible" to -3.3
    )

    private val intensifiers = mapOf(
        "very" to 1.5, "really" to 1.4, "so" to 1.4,
        "extremely" to 1.8, "super" to 1.6, "too" to 1.4
    )

    private val diminishers = mapOf(
        "slightly" to 0.7, "somewhat" to 0.8, "kinda" to 0.8,
        "kind" to 0.85, "little" to 0.75, "bit" to 0.75
    )

    private val negations = setOf(
        "not", "no", "never", "none", "cant", "can't", "cannot",
        "wont", "won't", "dont", "don't", "isnt", "isn't",
        "wasnt", "wasn't"
    )

    data class Result(
        val label: String,
        val score: Double,
        val confidence: Double
    )

    fun analyze(text: String): Result {
        val tokens = tokenize(text)
        if (tokens.isEmpty()) return Result("Neutral", 0.0, 0.0)

        var raw = 0.0
        var hits = 0

        for (i in tokens.indices) {
            val word = tokens[i]
            val base = lexicon[word] ?: continue

            val previousWords = listOfNotNull(
                tokens.getOrNull(i - 1),
                tokens.getOrNull(i - 2),
                tokens.getOrNull(i - 3)
            )

            var weight = 1.0

            previousWords.forEach {
                weight *= intensifiers[it] ?: 1.0
                weight *= diminishers[it] ?: 1.0
            }

            val isNegated = previousWords.any { it in negations }
            val adjusted = if (isNegated) -base else base

            raw += adjusted * weight
            hits++
        }

        if (hits == 0) return Result("Neutral", 0.0, 0.1)

        val norm = clamp(raw / 4.0, -1.0, 1.0)

        val label = when {
            norm >= 0.15 -> "Positive"
            norm <= -0.15 -> "Negative"
            else -> "Neutral"
        }

        val confidence = clamp(
            0.35 + (abs(norm) * 0.5) + min(hits, 3) * 0.05,
            0.0,
            1.0
        )

        return Result(label, norm, confidence)
    }

    private fun tokenize(text: String): List<String> =
        text.lowercase()
            .replace(Regex("[^a-z'\\s]"), " ")
            .replace("’", "'")
            .split(Regex("\\s+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

    private fun clamp(value: Double, minValue: Double, maxValue: Double): Double {
        return max(minValue, min(maxValue, value))
    }
}