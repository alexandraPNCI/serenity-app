package com.example.serenity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.serenity.data.database.AppDatabase
import com.example.serenity.data.repository.JournalRepository
import com.example.serenity.viewmodel.JournalViewModel
import com.example.serenity.ui.navigation.AppNavigation
import com.example.serenity.ui.theme.SerenityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "serenity.db"
        ).build()

        val repo = JournalRepository(db.journalDao())
        val viewModel = JournalViewModel(repo)

        setContent {
            SerenityTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
