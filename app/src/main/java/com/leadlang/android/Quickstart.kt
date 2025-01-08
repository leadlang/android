package com.leadlang.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leadlang.android.ui.theme.LeadAndroidTheme
import com.leadlang.android.utils.getTarget

class Quickstart: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = this
        val supported = !getTarget().isNullOrBlank()

        enableEdgeToEdge()
        setContent {
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

                            Text(
                                text = "The Lead Programming Language",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                ),
                                modifier = Modifier.padding(top = 30.dp)
                            )
                            Text(
                                text = "${getTarget()}",
                                modifier = Modifier.padding(top = 10.dp, bottom = 100.dp)
                            )

                            Button(
                                onClick = {
                                    val intent = Intent(ctx, Download::class.java)
                                    startActivity(intent)
                                    ctx.finish()
                                },
                                enabled = supported
                            ) {
                                if (supported) {
                                    Text("Lets get started")
                                } else {
                                    Text("Unsupported Platform")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}