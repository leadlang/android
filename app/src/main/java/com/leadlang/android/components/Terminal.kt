package com.leadlang.android.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import androidx.compose.ui.Modifier

@SuppressLint("SetJavaScriptEnabled")
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

      webview.loadUrl(mUrl)
    },
    modifier = modifier
  )
}

@Composable
fun Terminal(modifier: Modifier) {
  WebView(modifier)
}