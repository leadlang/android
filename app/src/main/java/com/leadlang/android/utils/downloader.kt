package com.leadlang.android.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.leadlang.android.MainActivity
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

fun downloadChunk(url: String, startByte: Long, endByte: Long, progressCallback: (Int) -> Unit): ByteArray {
  val connection = URL(url).openConnection() as HttpURLConnection
  connection.requestMethod = "GET"
  connection.setRequestProperty("Range", "bytes=$startByte-$endByte")

  val outputStream = ByteArrayOutputStream()
  val buffer = ByteArray((endByte - startByte + 1).toInt())

  if (connection.responseCode == HttpURLConnection.HTTP_PARTIAL) {
    val inputStream = BufferedInputStream(connection.inputStream)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
      outputStream.write(buffer, 0, bytesRead)

      progressCallback(bytesRead)
    }
    inputStream.close()
    Log.i("DWNL", "Chunk downloaded successfully")
  } else {
    Log.e("DWNL", "Failed to download chunk: HTTP ${connection.responseCode}")
  }

  connection.disconnect()
  return outputStream.toByteArray()
}

fun getFileSize(url: String): Long {
  val connection = URL(url).openConnection() as HttpURLConnection
  connection.requestMethod = "HEAD"
  connection.connect()
  val contentLengthString = connection.getHeaderField("Content-Length")

  connection.disconnect()

  return contentLengthString.toLongOrNull() ?: throw Exception("Failed to retrieve file size")
}

fun download(url: String, destination: String, progressCallback: (Float) -> Unit) {
  val numThreads = 8
  val executor = Executors.newFixedThreadPool(numThreads + 1)
  val futures = mutableListOf<Future<ByteArray>>()

  try {
    val queue = LinkedBlockingQueue<Int>()

    val fileSize = getFileSize(url)
    val chunkSize = fileSize / numThreads
    val file = File(destination)
    file.createNewFile()

    for (i in 0 until numThreads) {
      val startByte = i * chunkSize
      val endByte = if (i == numThreads - 1) fileSize - 1 else (startByte + chunkSize - 1)

      val future = executor.submit(Callable {
        downloadChunk(url, startByte, endByte) { int ->
          queue += int
        }
      })
      futures.add(future)
    }

    var totalDownloaded = 0
    var last = 0F;
    executor.submit(Callable {
      while (true) {
        val progint = queue.take()

        if (progint != null) {
          totalDownloaded += progint
          val prog = totalDownloaded.toFloat() / fileSize.toFloat()

          if (prog > (last + 0.01F)) {
            last = prog
            progressCallback(
              prog
            )
          }
        }
      }
    })

    for (future in futures) {
      val data = future.get()
      file.writeBytes(data)
      progressCallback(1.0F)
    }
    Log.i("DWNL", "Download completed successfully!")
  } catch (e: Exception) {
    Log.e("DWNL", "Error during download: ${e.message}")
  } finally {
    executor.shutdown()
  }
}

var runningLeadmanInstall = false

fun downloadInstallLeadman(activity: Activity, prog: (Float?) -> Unit, update: (String) -> Unit) {
  if (!runningLeadmanInstall) {
    runningLeadmanInstall = true
    thread {
      download(makeDwnUri(), getLeadmanDest(activity), prog)

      prog(null)
      update("Installing...")

      Thread.sleep(2000)

      createPaths(activity)

      Thread.sleep(2000)

      activity.startActivity(Intent(activity, MainActivity::class.java))
      activity.finish()
    }
  }
}