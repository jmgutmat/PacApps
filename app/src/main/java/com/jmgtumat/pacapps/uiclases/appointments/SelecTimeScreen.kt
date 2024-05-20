package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectTimeScreen(
    availableTimes: List<String>,
    onTimeSelected: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(availableTimes) { time ->
                TimeItem(
                    time = time,
                    onTimeSelected = { onTimeSelected(time) }
                )
            }
        }
    }
}

@Composable
fun TimeItem(
    time: String,
    onTimeSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onTimeSelected)
            .background(Color.LightGray, shape = CircleShape)
    ) {
        Text(
            text = time,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSelectTimeScreen() {
    SelectTimeScreen(
        availableTimes = listOf("9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM"),
        onTimeSelected = { }
    )
}
