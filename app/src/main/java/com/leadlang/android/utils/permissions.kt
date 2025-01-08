package com.leadlang.android.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.leadlang.android.R

fun requestPermission(ctx: Activity) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    if (!Environment.isExternalStorageManager()) {
      val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)

      val notificationManager = NotificationManagerCompat.from(ctx)

      val name = "Permission"
      val descriptionText = "Permission related notifications"
      val importance = NotificationManager.IMPORTANCE_HIGH
      val mChannel = NotificationChannel("permission", name, importance)
      mChannel.description = descriptionText

      notificationManager.createNotificationChannel(mChannel)

      val notif = NotificationCompat.Builder(ctx, "permission")
        .setSmallIcon(IconCompat.createWithResource(ctx, R.drawable.lead_lang))
        .setContentTitle("Allow Access to Files")
        .setContentText("You must provide access to all files")
        .build()

      notificationManager.notify(1, notif)

      ctx.startActivityForResult(intent, 1)
    }
  } else {
    ActivityCompat.requestPermissions(ctx, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
  }
}