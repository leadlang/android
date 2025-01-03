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

fun getArch(): String? {
  val abi = Build.SUPPORTED_ABIS[0];
  return when (abi) {
    "armeabi-v7a" -> "ARMv7"
    "arm64-v8a" -> "ARMv8"
    "x86" -> "x86"
    "x86_64" -> "x86_64"
    else -> null
  }
}