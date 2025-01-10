package com.leadlang.android.utils

import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.OutputStream
import kotlin.concurrent.thread

class ProcessExecutor(private val webview: WebView) {
  private var process: Process? = null
  private var outputStream: OutputStream? = null
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
      builder.redirectError(ProcessBuilder.Redirect.PIPE)
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

    process = builder.start()
    outputStream = process!!.outputStream!!
    reader = process!!.inputStream!!.bufferedReader()

    thread {
      while (true) {
        try {
          process!!.exitValue()
          break
        } catch (e: Exception) {
          try {
            val line = reader!!.readLine();

            if (line != null) {
              webview.post {
                webview.evaluateJavascript("globalThis.readProcResp(`${line.replace("\\", "\\\\").replace("`", "\\`")}`)") {

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
    outputStream!!.write(data.toByteArray())
    outputStream!!.flush()
  }
}

fun runBinary(ctx: Context, library: String, pwd: String, webview: WebView): Process {
  val libloader = "${ctx.applicationInfo.nativeLibraryDir}/libloader.so"

  val builder = ProcessBuilder(
      libloader
    )
    .directory(File(pwd))
    .redirectErrorStream(true)

  if (VERSION.SDK_INT > Build.VERSION_CODES.O) {
    builder.redirectError(ProcessBuilder.Redirect.PIPE)
    builder.redirectInput(ProcessBuilder.Redirect.PIPE)
    builder.redirectInput(ProcessBuilder.Redirect.PIPE)
  }

  builder.environment()["LOAD_DLL"] = library

  return builder.start()!!
}