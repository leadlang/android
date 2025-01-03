package com.leadlang.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.leadlang.android.ui.theme.LeadAndroidTheme
import com.leadlang.android.utils.leadExists

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!leadExists(this)) {
      val navigate = Intent(this, Quickstart::class.java)
      startActivity(navigate)
      this.finish()
    }

    enableEdgeToEdge()
    setContent {
      LeadAndroidTheme {
        var currentPage by remember {
          mutableIntStateOf(0)
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            NavigationBar {
              NavigationBarItem(selected = currentPage == 0, onClick = {
                currentPage = 0
              }, label = { Text("Dashboard") }, icon = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.dashboard), contentDescription = "Dashboard logo")
              })
              NavigationBarItem(selected = currentPage == 1, onClick = {
                currentPage = 1
              }, icon = {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_terminal_24), contentDescription = "Terminal logo")
              },
                label = { Text("Terminal") }
              )
              NavigationBarItem(selected = currentPage == 2, onClick = {
                currentPage = 2
              }, label = { Text("Debug") }, icon = {
                Icon(imageVector = Icons.Sharp.Info, contentDescription = "Information")
              })
            }
          }
        ) { innerPadding ->
          if (currentPage == 0) {
            Text("Dashboard", Modifier.padding(innerPadding))
          } else if (currentPage == 1) {
            Text("Terminal", Modifier.padding(innerPadding))
          } else {
            Text("Debug Info", Modifier.padding(innerPadding))
          }
        }
      }
    }
  }
}