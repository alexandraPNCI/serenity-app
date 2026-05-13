package com.example.serenity

import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.serenity.viewmodel.SentimentAnalyzer

class SentimentAnalyzerTest {

    @Test
    fun positiveText_returnsPositiveScore() {
        val result = SentimentAnalyzer.analyze("I feel happy and calm")
        assertEquals(1.0, result, 0.0)
    }

    @Test
    fun negativeText_returnsNegativeScore() {
        val result = SentimentAnalyzer.analyze("I feel sad and lonely")
        assertEquals(-1.0, result, 0.0)
    }

    @Test
    fun neutralText_returnsNeutralScore() {
        val result = SentimentAnalyzer.analyze("I went outside")
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun mixedText_returnsNeutralScore() {
        val result = SentimentAnalyzer.analyze("I am happy but also tired")
        assertEquals(0.0, result, 0.0)
    }
}
