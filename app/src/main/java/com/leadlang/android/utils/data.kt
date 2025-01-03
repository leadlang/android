package com.leadlang.android.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

const val TAG = "MyActivity"

fun leadExists(ctx: Context): Boolean {
  val cache = ctx.dataDir
  val lead = "$cache/leadlang"

  return Path(lead).exists()
}

fun getTarget(): String? {
  val abi = Build.SUPPORTED_ABIS[0];
  return when (abi) {
    "armeabi-v7a" -> "armv7-linux-androideabi"
    "arm64-v8a" -> "aarch64-linux-android"
    "x86" -> "i686-linux-android"
    "x86_64" -> "x86_64-linux-android"
    else -> null
  }
}

fun getLeadmanDest(ctx: Context): Uri {
  val cache = ctx.dataDir
  val lead = "$cache/leadlang"

  Path(lead).createDirectory()

  return "$lead/libleadman.so".toUri()
}

fun makeDwnUri(): Uri {
  val target = getTarget()!!

  return "https://github.com/leadlang/lead/releases/latest/download/libleadman_$target.so".toUri()
}