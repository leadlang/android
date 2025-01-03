package com.leadlang.android.utils

import android.content.Context
import android.util.Log
import java.util.concurrent.TimeUnit

fun executeLibLoader(ctx: Context) {
  val libloader = "${ctx.applicationInfo.nativeLibraryDir}/libloader.so"

  val proc = ProcessBuilder(
    libloader
  )
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .start();
  proc.waitFor(3, TimeUnit.SECONDS)

  val value = proc.inputStream.bufferedReader().readText()

  Log.d("MyActivity", "Ran Command\n${value}")
}