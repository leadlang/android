package com.leadlang.android.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import okio.FileSystem
import okio.Path.Companion.toPath

class BridgeInterface(private val webview: WebView) {
  @JavascriptInterface
  fun defaultPath(): String {
    return ContextCompat.getDataDir(webview.context).toString()
  }

  @JavascriptInterface
  fun validatePath(path: String): Boolean {
    return FileSystem.SYSTEM.exists(path.toPath());
  }

  @JavascriptInterface
  suspend fun getCwd(): String {
    return ""
  }
}

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebView(modifier: Modifier) {
  val mUrl = "http://192.168.29.218:5173/"

  AndroidView(
    factory = {
      WebView(it).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
      }
    },
    update = { webview ->
      webview.settings.javaScriptEnabled = true
      webview.addJavascriptInterface(BridgeInterface(webview), "Kotlin")

      webview.loadUrl(mUrl)
    },
    modifier = modifier
  )
}

@Composable
fun Terminal(modifier: Modifier) {
  WebView(modifier)
}