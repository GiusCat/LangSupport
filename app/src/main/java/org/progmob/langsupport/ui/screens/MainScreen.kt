package org.progmob.langsupport.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.progmob.langsupport.R
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.ui.dialogs.SettingsDialog
import org.progmob.langsupport.ui.theme.LangSupportTheme

@Composable
fun MainScreen(
    viewModel: DataViewModel,
    modifier: Modifier = Modifier
) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val screens = listOf(
        stringResource(id = R.string.prefs),
        stringResource(id = R.string.search),
        stringResource(id = R.string.stats)
    )

    Scaffold(
        topBar = { TopBar(onGearClick = { showSettingsDialog = true }) },
        bottomBar = { BottomBar(onClick = { navController.navigate(screens[it]) }) },
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = screens[1],
            modifier = Modifier.padding(it)
        ) {
            composable(screens[0]) { FavouriteScreen(viewModel = viewModel) }
            composable(screens[1]) { SearchScreen(viewModel = viewModel) }
            composable(screens[2]) { StatsScreen() }
        }
    }

    AnimatedVisibility(visible = showSettingsDialog) {
        SettingsDialog(
            defaultLanguage = viewModel.getTranslateLanguage(),
            email = viewModel.getUserEmail(),
            onLanguageChange = { viewModel.setTranslateLanguage(it) },
            onLogout = { viewModel.signOutUser() },
            onDismiss = { showSettingsDialog = false })
    }
}


@Composable
fun TopBar(
    onGearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            IconButton(onClick = onGearClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Composable
fun BottomBar(
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableIntStateOf(1) }
    val items = listOf(R.string.prefs, R.string.search, R.string.stats)
    val icons = listOf(Icons.Outlined.Star, Icons.Outlined.Search, Icons.Default.Info)

    NavigationBar(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = stringResource(id = item)) },
                label = { Text(stringResource(id = item)) },
                selected = selected == index,
                onClick = {
                    selected = index
                    onClick(selected)
                }
            )
        }
    }
}