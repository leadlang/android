package com.leadlang.android.utils

import android.content.Context
import android.os.Build
import android.util.Log
import java.util.concurrent.TimeUnit

fun runLib(ctx: Context, library: String): Process {
  val libloader = "${ctx.applicationInfo.nativeLibraryDir}/libloader.so"

  val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    ProcessBuilder(
      libloader
    )
      .redirectOutput(ProcessBuilder.Redirect.PIPE)
      .redirectInput(ProcessBuilder.Redirect.PIPE)
      .redirectError(ProcessBuilder.Redirect.PIPE)
  } else {
    TODO("VERSION.SDK_INT < O")
  }

  builder.environment()["LOAD_DLL"] = library

  return builder.start()!!
}