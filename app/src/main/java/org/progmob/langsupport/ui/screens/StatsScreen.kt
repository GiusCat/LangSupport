package org.progmob.langsupport.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatsScreen(modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxSize()) {
        Text(text = "Work in Progress")
    }
}