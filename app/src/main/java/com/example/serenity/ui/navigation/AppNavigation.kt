package com.example.serenity.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.serenity.data.repository.UserRepository
import com.example.serenity.ui.auth.LoginScreen
import com.example.serenity.ui.auth.RegisterScreen
import com.example.serenity.ui.entries.EntriesScreen
import com.example.serenity.ui.history.HistoryScreen
import com.example.serenity.ui.home.HomeScreen
import com.example.serenity.ui.journal.JournalScreen
import com.example.serenity.ui.splash.SplashScreen
import com.example.serenity.viewmodel.JournalViewModel

@Composable
fun AppNavigation(
    viewModel: JournalViewModel,
    userRepository: UserRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                userRepository = userRepository,
                onLoginSuccess = { user ->
                    viewModel.setUser(user.id)

                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                userRepository = userRepository,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToJournal = {
                    navController.navigate("journal")
                },
                onNavigateToEntries = {
                    navController.navigate("entries")
                },
                onNavigateToHistory = {
                    navController.navigate("history")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("journal") {
            JournalScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("entries") {
            EntriesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}