package com.seal.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seal.app.viewmodel.DownloadViewModel

@Composable
fun SettingsScreen(viewModel: DownloadViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, start = 8.dp)
            )
        }

        item { SettingsSectionHeader("General") }

        item {
            SettingsItem(
                icon = Icons.Outlined.Tune,
                title = "General",
                subtitle = "App behavior & notifications"
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsSectionHeader("Storage") }

        item {
            SettingsItem(
                icon = Icons.Outlined.FolderOpen,
                title = "Download directory",
                subtitle = viewModel.downloadPath
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsSectionHeader("Format") }

        item {
            SettingsItem(
                icon = Icons.Outlined.HighQuality,
                title = "Format settings",
                subtitle = "Audio: MP3 320kbps · Video: Best MP4"
            )
        }

        item {
            SettingsItem(
                icon = Icons.Outlined.Subtitles,
                title = "Subtitles",
                subtitle = "Auto-embed subtitles when available"
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsSectionHeader("About") }

        item {
            SettingsItem(
                icon = Icons.Outlined.Info,
                title = "Seal",
                subtitle = "v1.0.0 · Powered by yt-dlp"
            )
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(title, color = MaterialTheme.colorScheme.onSurface)
        },
        supportingContent = {
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
