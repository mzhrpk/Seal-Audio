package com.seal.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seal.app.ui.components.DownloadBottomSheet
import com.seal.app.viewmodel.DownloadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DownloadViewModel, sharedUrl: String?) {
    var urlText by remember { mutableStateOf(sharedUrl ?: "") }
    var showSheet by remember { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current
    val downloadState by viewModel.downloadState.collectAsState()

    if (showSheet) {
        DownloadBottomSheet(
            url = urlText,
            onDismiss = { showSheet = false },
            onDownload = { type ->
                showSheet = false
                viewModel.startDownload(urlText, type)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // App Title
            Text(
                text = "Seal",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 72.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = "Media Downloader",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // URL Input
            OutlinedTextField(
                value = urlText,
                onValueChange = { urlText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Video link") },
                placeholder = { Text("https://...") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Download Status
            AnimatedVisibility(visible = downloadState.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = downloadState,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // FABs — bottom right
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Small Paste FAB
            SmallFloatingActionButton(
                onClick = {
                    clipboard.getText()?.text?.let { urlText = it }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(
                    Icons.Filled.ContentPaste,
                    contentDescription = "Paste",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Primary Download FAB
            FloatingActionButton(
                onClick = {
                    if (urlText.isNotBlank()) showSheet = true
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Filled.Download, contentDescription = "Download")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
