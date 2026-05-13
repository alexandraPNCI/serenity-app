package com.example.serenity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.serenity.data.database.AppDatabase
import com.example.serenity.data.repository.JournalRepository
import com.example.serenity.data.repository.UserRepository
import com.example.serenity.ui.navigation.AppNavigation
import com.example.serenity.ui.theme.SerenityTheme
import com.example.serenity.viewmodel.JournalViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "serenity.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val journalRepo = JournalRepository(db.journalDao())
        val userRepo = UserRepository(db.userDao())

        val viewModel = JournalViewModel(journalRepo)

        setContent {
            SerenityTheme {
                AppNavigation(viewModel, userRepo)
            }
        }
    }
}
