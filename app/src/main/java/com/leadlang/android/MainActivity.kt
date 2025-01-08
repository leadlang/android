package com.leadlang.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.leadlang.android.components.Dashboard
import com.leadlang.android.components.Terminal
import com.leadlang.android.ui.theme.LeadAndroidTheme
import com.leadlang.android.utils.leadExists
import com.leadlang.android.utils.requestPermission
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onActivityReenter(resultCode: Int, data: Intent?) {
    super.onActivityReenter(resultCode, data)

    if (resultCode == 1) {
      val notif = NotificationManagerCompat.from(this)
      notif.cancel(1)
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)

    if (!leadExists(this)) {
      val navigate = Intent(this, Quickstart::class.java)
      startActivity(navigate)
      this.finish()
    }

    requestPermission(this)

    enableEdgeToEdge()
    setContent {
      LeadAndroidTheme {
        val scope = rememberCoroutineScope()
        var currentPage by remember {
          mutableIntStateOf(0)
        }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var showBottomSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState(
          skipPartiallyExpanded = false,
        )

        ModalNavigationDrawer(
          drawerContent = {
            ModalDrawerSheet {
              NavigationDrawerItem(
                label = { Text("Lead Android") },
                selected = false,
                onClick = { showBottomSheet = true },
                modifier = Modifier.padding(10.dp),
                icon = {
                  Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.lead_lang),
                    contentDescription = "Home",
                    modifier = Modifier.size(20.dp)
                  )
                },
                badge = {
                  Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                  ) {
                    Text("Update")
                  }
                }
              )
              HorizontalDivider()
              NavigationDrawerItem(
                label = { Text("Home") },
                selected = true,
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(10.dp),
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }
              )
              NavigationDrawerItem(
                label = { Text("Versions") },
                selected = false,
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Home") },
                badge = {
                  Badge {
                    Text("0")
                  }
                }
              )
            }
          },
          drawerState = drawerState
        ) {

          Scaffold(
            modifier = Modifier.fillMaxSize()
          ) { innerPadding ->
            NavigationSuiteScaffold(
              navigationSuiteItems = {
                item(
                  icon = {
                    Icon(
                      Icons.Filled.Menu,
                      contentDescription = "Menu"
                    )
                  },
                  label = { Text("Menu") },
                  selected = false,
                  onClick = {
                    scope.launch {
                      drawerState.apply {
                        if (isClosed) open() else close()
                      }
                    }
                  }
                )

                item(
                  icon = {
                    Icon(
                      imageVector = ImageVector.vectorResource(R.drawable.dashboard),
                      contentDescription = "Dashboard logo"
                    )
                  },
                  label = { Text("Dashboard") },
                  selected = currentPage == 0,
                  onClick = { currentPage = 0 }
                )

                item(
                  selected = currentPage == 1,
                  onClick = {
                    currentPage = 1
                  },
                  icon = {
                    Icon(
                      imageVector = ImageVector.vectorResource(R.drawable.baseline_terminal_24),
                      contentDescription = "Terminal logo"
                    )
                  },
                  label = { Text("Terminal") }
                )

                item(
                  selected = currentPage == 2,
                  onClick = {
                    currentPage = 2
                  },
                  label = { Text("Debug") },
                  icon = {
                    Icon(imageVector = Icons.Sharp.Info, contentDescription = "Information")
                  }
                )
              },
              modifier = Modifier.fillMaxSize()
            ) {
              if (showBottomSheet) {
                ModalBottomSheet(
                  modifier = Modifier.fillMaxHeight(),
                  sheetState = sheetState,
                  onDismissRequest = { showBottomSheet = false }
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                      .padding(top = 3.dp)
                      .fillMaxWidth()
                  ) {
                    Image(
                      imageVector = ImageVector.vectorResource(R.drawable.lead_lang),
                      contentDescription = "Lead Language",
                    )
                  }

                  Text(
                    "Lead Lang for Android",
                    textAlign = TextAlign.Center,
                    fontSize = 3.em,
                    modifier = Modifier
                      .padding(20.dp)
                      .fillMaxWidth()
                  )

                  Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                      .padding(start = 20.dp, end = 20.dp, top = 12.dp)
                      .fillMaxWidth()
                  ) {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.rocket), contentDescription = "Rocket Icon")
                    Text(
                      "Update Now",
                      modifier = Modifier.padding(start = 2.dp)
                    )
                  }
                }
              }

              when (currentPage) {
                0 -> {
                  Dashboard(
                    modifier = Modifier.padding(innerPadding)
                  )
                }
                1 -> {
                  Terminal(Modifier.padding(innerPadding))
                }
                else -> {
                  Text("Debug Info", Modifier.padding(innerPadding))
                }
              }
            }
          }
        }
      }
    }
  }
}