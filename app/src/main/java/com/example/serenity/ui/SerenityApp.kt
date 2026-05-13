package com.example.serenity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.serenity.ui.journal.JournalScreen
import com.example.serenity.ui.navigation.DashboardScreen
import com.example.serenity.ui.theme.SerenityBackground
import com.example.serenity.ui.theme.SerenityPrimary
import com.example.serenity.ui.theme.SerenityPrimarySoft
import com.example.serenity.ui.theme.SerenitySurface
import com.example.serenity.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerenityApp(viewModel: JournalViewModel) {

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = SerenityBackground,

        bottomBar = {
            NavigationBar(
                containerColor = SerenitySurface,
                tonalElevation = 8.dp
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Journal") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Journal"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SerenityPrimary,
                        selectedTextColor = SerenityPrimary,
                        indicatorColor = SerenityPrimarySoft
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Insights") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = "Insights"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SerenityPrimary,
                        selectedTextColor = SerenityPrimary,
                        indicatorColor = SerenityPrimarySoft
                    )
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(SerenityBackground)
        ) {
            when (selectedTab) {
                0 -> JournalScreen(
                    viewModel = viewModel,
                    onBack = { },
                    onLogout = { }
                )
                1 -> DashboardScreen(viewModel)
            }
        }
    }
}
