package com.jmgtumat.pacapps.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateSelector(
    context: Context,
    startDate: MutableState<Long?>,
    endDate: MutableState<Long?>
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    if (showStartDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                startDate.value = calendar.timeInMillis
                showStartDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                endDate.value = calendar.timeInMillis
                showEndDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Inicio")
            }
            Text(
                text = startDate.value?.toFormattedString() ?: "No seleccionada",
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { showEndDatePicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Fin")
            }
            Text(
                text = endDate.value?.toFormattedString() ?: "No seleccionada",
                modifier = Modifier.weight(1f)
            )
        }
    }
}


fun Long.toFormattedString(): String {
    return if (this != null) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))
    } else {
        "No seleccionada"
    }
}