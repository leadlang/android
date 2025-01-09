package com.leadlang.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Modifier
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath

class BridgeInterface(private val webview: WebView, private val launcher: ManagedActivityResultLauncher<Uri?, Uri?>) {
  @JavascriptInterface
  fun getFiles(dir: String): String {
    var ret = "\u001b[4mType\u001b[0m \u001B[4mFile\u001B[0m"

    for (entry in FileSystem.SYSTEM.list(dir.toPath())) {
      val typ = if (FileSystem.SYSTEM.metadata(entry.toFile().toOkioPath()).isDirectory) {
        "-d  "
      } else {
        "f-  "
      }

      val name = entry.name

      ret += "\n\u001b[1m$typ\u001B[0m $name"
    }

    return ret
  }

  @JavascriptInterface
  fun defaultPath(): String {
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
  }

  @JavascriptInterface
  fun validatePath(path: String): Boolean {
    return FileSystem.SYSTEM.exists(path.toPath())
  }

  @JavascriptInterface
  fun getCwd(): String {
    launcher.launch(null)
    return ""
  }
}

fun getAbsolutePathFromUri(context: Context, uri: Uri): String? {
  val documentId = DocumentsContract.getTreeDocumentId(uri)
  val parts = documentId.split(":")
  if (parts.size == 2) {
    val storageType = parts[0]
    val relativePath = parts[1]

    return if (storageType == "primary") {
      "/storage/emulated/0/$relativePath"
    } else {
      val externalDirs = context.getExternalFilesDirs(null)
      for (file in externalDirs) {
        if (file.absolutePath.contains(storageType)) {
          val basePath = file.absolutePath.substringBefore("/Android")
          return "$basePath/$relativePath"
        }
      }
      null
    }
  }
  return null
}


@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebView(context: Context, modifier: Modifier) {
  val debug = isDebuggable(context)
  var webView: WebView? = null
  
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocumentTree()
  ) { uri ->
    if (uri != null) {
      // Persist permissions
      context.contentResolver.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
      )

      val path = getAbsolutePathFromUri(context, uri)
      Toast.makeText(context, "Selected: $path", Toast.LENGTH_SHORT).show()
      webView!!.evaluateJavascript("""
        globalThis.setDir("${path.toString().replace("\"", "\\\"").replace("\\", "\\\\")}")
      """.trimIndent()) {

      }
    }
  }

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
      webview.addJavascriptInterface(BridgeInterface(webview, launcher), "Kotlin")

      webView = webview

      if (debug) {
        val mUrl = "http://192.168.29.218:5173/"
        webview.loadUrl(mUrl)
      } else {
        val assetLoader = WebViewAssetLoader.Builder()
          .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
          .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(context))
          .build()

        webview.webViewClient = LocalContentWebViewClient(assetLoader)
        webview.loadUrl("https://appassets.androidplatform.net/assets/index.html")
      }
    },
    modifier = modifier
  )
}

private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
  override fun shouldInterceptRequest(
    view: WebView,
    request: WebResourceRequest
  ): WebResourceResponse? {
    return assetLoader.shouldInterceptRequest(request.url)
  }
}

fun isDebuggable(context: Context): Boolean {
  return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}

@Composable
fun Terminal(context: Context, modifier: Modifier) {
  WebView(context, modifier)
}