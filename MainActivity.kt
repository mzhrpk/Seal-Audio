package com.seal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.seal.app.ui.screens.HomeScreen
import com.seal.app.ui.screens.LibraryScreen
import com.seal.app.ui.screens.SettingsScreen
import com.seal.app.ui.theme.SealTheme
import com.seal.app.viewmodel.DownloadViewModel

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Home")
    object Settings : Screen("settings", "Settings")
    object Library : Screen("library", "Library")
}

class MainActivity : ComponentActivity() {
    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Handle shared intent (URL from browser/YouTube)
        val sharedUrl = intent?.getStringExtra(android.content.Intent.EXTRA_TEXT)

        setContent {
            SealTheme {
                SealApp(
                    viewModel = viewModel,
                    sharedUrl = sharedUrl
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SealApp(viewModel: DownloadViewModel, sharedUrl: String?) {
    val navController = rememberNavController()

    val navItems = listOf(
        Triple(Screen.Home, Icons.Filled.Home, "Home"),
        Triple(Screen.Settings, Icons.Filled.Settings, "Settings"),
        Triple(Screen.Library, Icons.Outlined.FolderOpen, "Library"),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = androidx.compose.ui.unit.dp * 0
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDest = navBackStackEntry?.destination

                navItems.forEach { (screen, icon, label) ->
                    NavigationBarItem(
                        selected = currentDest?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = viewModel, sharedUrl = sharedUrl)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
            composable(Screen.Library.route) {
                LibraryScreen()
            }
        }
    }
}
