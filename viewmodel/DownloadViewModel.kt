package com.seal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seal.app.ui.components.DownloadType
import com.seal.app.util.YtDlpRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DownloadViewModel(application: Application) : AndroidViewModel(application) {

    private val _downloadState = MutableStateFlow("")
    val downloadState: StateFlow<String> = _downloadState

    val downloadPath: String = "Downloads/Seal"

    fun startDownload(url: String, type: DownloadType) {
        if (url.isBlank()) return

        viewModelScope.launch {
            _downloadState.value = "⏳ Starting download…"

            val typeStr = if (type == DownloadType.AUDIO) "audio" else "video"

            val success = YtDlpRunner.download(
                context = getApplication(),
                url = url,
                type = typeStr
            ) { line ->
                // Parse yt-dlp's progress output
                val progressLine = when {
                    line.contains("[download]") -> line
                    line.contains("[ExtractAudio]") -> "🎵 Extracting audio…"
                    line.contains("[ffmpeg]") -> "🔧 Post-processing…"
                    line.contains("ERROR") -> "❌ $line"
                    else -> line
                }
                _downloadState.value = progressLine
            }

            _downloadState.value = if (success) {
                "✅ Download complete! Saved to Downloads/Seal"
            } else {
                "❌ Download failed. Check the URL and try again."
            }
        }
    }
}
