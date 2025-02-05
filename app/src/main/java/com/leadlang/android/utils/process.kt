package com.leadlang.android.utils

import android.os.Build
import android.os.Build.VERSION
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import kotlin.concurrent.thread

class ProcessExecutor(private val webview: WebView) {
  private var process: Process? = null
  private var outputStream: BufferedWriter? = null
  private var reader: BufferedReader? = null
  private var context = webview.context

  @JavascriptInterface
  fun execute(pwd: String, args: String) {
    val finalArgs =  mutableListOf("${context.applicationInfo.nativeLibraryDir}/libloader.so")

    args.split(" ").toList().forEach {
      finalArgs += it
    }

    val builder = ProcessBuilder(finalArgs)
      .directory(File(pwd))

    if (VERSION.SDK_INT > Build.VERSION_CODES.O) {
      builder.redirectInput(ProcessBuilder.Redirect.PIPE)
      builder.redirectOutput(ProcessBuilder.Redirect.PIPE)
    }

    builder.redirectErrorStream(true)

    when (finalArgs[1]) {
      "leadman" -> {
        builder.environment()["LOAD_DLL"] = "${ContextCompat.getDataDir(context)}/leadlang/libleadman.so"
      }
      else -> {}
    }

    builder.environment()["LEAD_HOME"] = "${ContextCompat.getDataDir(context)}/leadlang"

    process?.destroy()

    process = builder.start()
    outputStream = process!!.outputStream!!.bufferedWriter()
    reader = process!!.inputStream!!.bufferedReader()

    thread {
      while (true) {
        try {
          process!!.exitValue()
          break
        } catch (e: Exception) {
          try {
            val buffer = CharArray(1024) // Buffer size of 1KB

            val bytesRead: Int = reader!!.read(buffer)
            if (bytesRead > 0) {
              val data = String(buffer.sliceArray(0..bytesRead))
              webview.post {
                webview.evaluateJavascript("globalThis.readProcResp(`${data.replace("\\", "\\\\").replace("`", "\\`")}`)") {
                }
              }
            }
          } catch (e: Exception) {
            break
          }
        }
      }

      webview.post {
        webview.evaluateJavascript("globalThis.procEnded()") {

        }
      }
    }
  }

  @JavascriptInterface
  fun sendToProcess(data: String) {
    outputStream!!.write(data)
    outputStream!!.flush()
    outputStream!!.close()
  }
}

