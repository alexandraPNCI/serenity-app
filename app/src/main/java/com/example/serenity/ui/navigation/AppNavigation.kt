package com.example.serenity.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.serenity.ui.home.HomeScreen
import com.example.serenity.ui.journal.JournalScreen
import com.example.serenity.ui.history.HistoryScreen

import com.example.serenity.viewmodel.JournalViewModel

@Composable
fun AppNavigation(viewModel: JournalViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                onNavigateToJournal = { navController.navigate("journal") },
                onNavigateToHistory = { navController.navigate("history") }
            )
        }

        composable("journal") {
            JournalScreen(viewModel)
        }

        composable("historyScreen") {
    HistoryScreen(viewModel)
}

    }
}
