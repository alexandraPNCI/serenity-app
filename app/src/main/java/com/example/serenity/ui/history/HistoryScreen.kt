package com.example.serenity.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.serenity.viewmodel.JournalViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.math.roundToInt
import android.graphics.Color as AndroidColor

@Composable
fun HistoryScreen(
    viewModel: JournalViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val entries = viewModel.entries.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF7F3FF),
                        Color(0xFFEAF7FF),
                        Color.White
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Insights",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = { showLogoutDialog = true }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (entries.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("💙", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No insights yet", fontWeight = FontWeight.Bold)
                    Text(
                        text = "Start journaling to unlock your private mood insights.",
                        color = Color.DarkGray
                    )
                }
            }
            return@Column
        }

        val positive = entries.count { it.aiSentiment == "Positive" }
        val neutral = entries.count { it.aiSentiment == "Neutral" }
        val negative = entries.count { it.aiSentiment == "Negative" }

        val smartInsight = viewModel.generateSmartInsight(entries)
        val scores = entries.mapNotNull { it.sentimentScore }
        val averageScore = if (scores.isNotEmpty()) scores.average() else 0.0

        val dominantMood = listOf(
            "Positive" to positive,
            "Neutral" to neutral,
            "Negative" to negative
        ).maxByOrNull { it.second }?.first ?: "Neutral"

        val positivePercent =
            if (entries.isNotEmpty()) ((positive.toDouble() / entries.size) * 100).roundToInt()
            else 0

        val recentEntries = entries.take(3)
        val recentAverage = recentEntries.mapNotNull { it.sentimentScore }.let {
            if (it.isNotEmpty()) it.average() else 0.0
        }

        val recentTrend = when {
            recentAverage > 0.25 -> "Positive"
            recentAverage < -0.25 -> "Low"
            else -> "Stable"
        }

        val currentStreak = calculateCurrentStreak(entries)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = "Smart Insight",
                        tint = Color(0xFF8B83F6)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text("Smart Mood Insight", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = smartInsight,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Entries",
                value = entries.size.toString(),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            SummaryCard(
                title = "Positive",
                value = "$positivePercent%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Avg Score",
                value = "%.2f".format(averageScore),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            SummaryCard(
                title = "Trend",
                value = recentTrend,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Main Mood",
                value = dominantMood,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            SummaryCard(
                title = "Streak",
                value = currentStreak,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Mood Distribution",
                        tint = Color(0xFF8B83F6)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text("Mood Distribution", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    factory = { context ->
                        val chart = PieChart(context)

                        val pieEntries = mutableListOf<PieEntry>()
                        if (positive > 0) pieEntries.add(PieEntry(positive.toFloat(), "Positive"))
                        if (neutral > 0) pieEntries.add(PieEntry(neutral.toFloat(), "Neutral"))
                        if (negative > 0) pieEntries.add(PieEntry(negative.toFloat(), "Negative"))

                        val dataSet = PieDataSet(pieEntries, "")
                        dataSet.colors = listOf(
                            AndroidColor.parseColor("#A5D6A7"),
                            AndroidColor.parseColor("#FFE082"),
                            AndroidColor.parseColor("#90CAF9")
                        )
                        dataSet.setDrawValues(false)

                        val pieData = PieData(dataSet)
                        pieData.setDrawValues(false)

                        chart.data = pieData
                        chart.setDrawEntryLabels(false)
                        chart.description.isEnabled = false
                        chart.legend.isEnabled = true
                        chart.setDrawHoleEnabled(true)
                        chart.holeRadius = 65f
                        chart.centerText = "${entries.size}\nEntries"
                        chart.animateY(1000)

                        chart
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        val sortedEntries = entries.sortedBy { it.timestamp }
        val trendValues = sortedEntries.mapIndexed { index, entry ->
            Entry(index.toFloat(), (entry.sentimentScore ?: 0.0).toFloat())
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Mood Trend",
                        tint = Color(0xFF8B83F6)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text("Mood Trend", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context ->
                        val chart = LineChart(context)

                        val dataSet = LineDataSet(trendValues, "Mood Score")
                        dataSet.color = AndroidColor.parseColor("#9FA8DA")
                        dataSet.setCircleColor(AndroidColor.parseColor("#9FA8DA"))
                        dataSet.lineWidth = 3f
                        dataSet.circleRadius = 5f
                        dataSet.setDrawValues(false)
                        dataSet.setDrawFilled(true)
                        dataSet.fillColor = AndroidColor.parseColor("#9FA8DA")
                        dataSet.fillAlpha = 60

                        chart.data = LineData(dataSet)
                        chart.description.isEnabled = false
                        chart.legend.isEnabled = false
                        chart.axisRight.isEnabled = false
                        chart.axisLeft.axisMinimum = -1f
                        chart.axisLeft.axisMaximum = 1f
                        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        chart.xAxis.setDrawGridLines(false)
                        chart.animateX(1000)

                        chart
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun calculateCurrentStreak(entries: List<com.example.serenity.data.model.JournalEntry>): String {
    if (entries.isEmpty()) return "0"

    val sorted = entries.sortedByDescending { it.timestamp }
    val firstMood = sorted.first().aiSentiment ?: sorted.first().manualMood ?: "Neutral"

    var streak = 0

    for (entry in sorted) {
        val mood = entry.aiSentiment ?: entry.manualMood ?: "Neutral"
        if (mood == firstMood) {
            streak++
        } else {
            break
        }
    }

    return "$streak $firstMood"
}