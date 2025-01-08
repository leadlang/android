package com.leadlang.android.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import okio.FileSystem
import okio.Path.Companion.toPath

fun leadExists(ctx: Context): Boolean {
  val cache = ContextCompat.getDataDir(ctx)
  val so = "$cache/leadlang/libleadman.so"

  return FileSystem.SYSTEM.exists(so.toPath())
}

fun createPaths(ctx: Context) {
  try {
    val cache = ContextCompat.getDataDir(ctx)
    val leadHome = "$cache/leadlang"

    FileSystem.SYSTEM.createDirectories("$leadHome/versions".toPath())
  } catch (e: Exception) {
    Log.w("PATH", e.toString())
  }
}

fun getTarget(): String? {
  for (abi in Build.SUPPORTED_ABIS) {
    val ret = when (abi) {
      "armeabi-v7a" -> "armv7-linux-androideabi"
      "arm64-v8a" -> "aarch64-linux-android"
      "x86" -> "i686-linux-android"
      "x86_64" -> "x86_64-linux-android"
      else -> null
    }

    if (ret != null) {
      return ret
    }
  }

  return null
}

fun getLeadmanDest(ctx: Context): String {
  val cache = ContextCompat.getDataDir(ctx)
  val lead = "$cache/leadlang"

  FileSystem.SYSTEM.createDirectory(lead.toPath())

  return "$lead/libleadman.so"
}

fun makeDwnUri(): String {
  val target = getTarget()!!

  return "https://github.com/leadlang/lead/releases/latest/download/libleadman_$target.so"
}