package com.leadlang.android

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leadlang.android.ui.theme.LeadAndroidTheme
import com.leadlang.android.utils.getLeadmanDest
import com.leadlang.android.utils.makeDwnUri

class Download: ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val download = this

    enableEdgeToEdge()
    setContent {
      var dwnProgValue by remember {
        mutableFloatStateOf(0.0F)
      }

      val downloader: DownloadManager = (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?)!!

      downloader.run {
        val request = DownloadManager.Request(makeDwnUri())
          .setDestinationUri(getLeadmanDest(download))

        downloader.enqueue(request)
      }

      LeadAndroidTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(innerPadding)
              .fillMaxSize()
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()
            ) {
              Image(
                imageVector = ImageVector.vectorResource(R.drawable.lead_lang),
                contentDescription = "Lead Language",
              )

              LinearProgressIndicator(
                progress = {
                  dwnProgValue
                },
                modifier = Modifier
                  .padding(top = 60.dp, bottom = 5.dp, start = 30.dp, end = 30.dp)
                  .height(12.dp)
                  .fillMaxWidth()
              )

              Text(
                text = "Downloading leadman..",
                style = TextStyle(
                  fontSize = 20.sp,
                ),
              )
            }
          }
        }
      }
    }
  }
}