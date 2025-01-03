package com.leadlang.android.utils

import android.content.Context
import android.os.Build
import kotlin.io.path.Path
import kotlin.io.path.exists

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

fun makeDwnUri(): String {
  val target = getTarget()!!

  return "https://github.com/leadlang/lead/releases/latest/download/libleadman_$target.so"
}