package com.seal.app.util

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object YtDlpRunner {

    /**
     * Copies the yt-dlp binary from assets to the app's files dir
     * and makes it executable. Call once on first launch.
     */
    suspend fun ensureBinaryReady(context: Context): File = withContext(Dispatchers.IO) {
        val binaryFile = File(context.filesDir, "yt-dlp")
        if (!binaryFile.exists() || binaryFile.length() == 0L) {
            context.assets.open("yt-dlp").use { input ->
                binaryFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        binaryFile.setExecutable(true)
        binaryFile
    }

    /**
     * Runs yt-dlp with the given arguments and streams output line by line.
     *
     * @param context   Android Context (for filesDir path)
     * @param url       The media URL to download
     * @param type      "audio" or "video"
     * @param onProgress callback with each output line
     * @return true if process exited with code 0
     */
    suspend fun download(
        context: Context,
        url: String,
        type: String,
        onProgress: (String) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {

        val binary = ensureBinaryReady(context)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ).absolutePath + "/Seal"
        File(downloadsDir).mkdirs()

        val args = if (type == "audio") {
            listOf(
                binary.absolutePath,
                "--no-playlist",
                "-x",                                         // extract audio
                "--audio-format", "mp3",
                "--audio-quality", "0",                       // 0 = best (320kbps VBR)
                "--embed-thumbnail",
                "--add-metadata",
                "--embed-metadata",
                "-o", "$downloadsDir/%(title)s.%(ext)s",
                url
            )
        } else {
            listOf(
                binary.absolutePath,
                "--no-playlist",
                "-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best",
                "--merge-output-format", "mp4",
                "-o", "$downloadsDir/%(title)s.%(ext)s",
                url
            )
        }

        val process = ProcessBuilder(args)
            .redirectErrorStream(true)
            .start()

        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                onProgress(line)
            }
        }

        process.waitFor() == 0
    }
}
