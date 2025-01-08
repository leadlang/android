package com.leadlang.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.leadlang.android.R

@Composable
fun Dashboard(modifier: Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier
        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        .fillMaxWidth()
        .clickable { },
    ) {
      Image(
        Icons.Filled.Build,
        contentDescription = "Build",
      )

      Column {
        Text("Update available")
        Text("An update is available")
      }
    }
  }
}

@Composable
fun EpicUpdateWidget(
  modifier: Modifier = Modifier,
  onUpdateClick: () -> Unit
) {
  Box(
    modifier = modifier
      .padding(16.dp)
      .clip(RoundedCornerShape(24.dp))
      .background(MaterialTheme.colorScheme.surfaceContainerHighest)
      .border(
        BorderStroke(3.dp, MaterialTheme.colorScheme.onPrimary),
        RoundedCornerShape(24.dp)
      )
      .clickable { onUpdateClick() }
  ) {
    Column(
      modifier = Modifier
        .padding(32.dp)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        painterResource(id = android.R.drawable.ic_delete),
        contentDescription = "Update Icon",
        modifier = Modifier
          .size(90.dp),
        tint = MaterialTheme.colorScheme.onPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Epic Update Available!",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onUpdateClick,
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .height(52.dp)
          .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
      ) {
        Text(
          "Update Now",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
      }
    }
  }
}
