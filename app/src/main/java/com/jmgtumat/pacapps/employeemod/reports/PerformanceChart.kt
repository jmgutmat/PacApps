package com.jmgtumat.pacapps.employeemod.reports

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jmgtumat.pacapps.viewmodels.CitaViewModel

@Composable
fun PerformanceChart(context: Context, citaViewModel: CitaViewModel) {
    // Obtenemos las citas reales desde el ViewModel
    val citas = citaViewModel.citas.value ?: emptyList()

    // Convertimos las citas a puntos de datos para el gráfico
    val points = citas.mapIndexed { index, cita ->
        Entry(index.toFloat(), cita.duracion.toFloat()) // Modificar la obtención de la duración real según la estructura de la cita
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Desempeño Mensual", style = MaterialTheme.typography.titleMedium)
            LineChart(context).apply {
                data = LineData(LineDataSet(points, "Performance").apply {
                    color = Color.Blue.toArgb()
                    setCircleColor(Color.Blue.toArgb())
                    setDrawValues(false)
                })
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                axisLeft.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
                invalidate()
            }
        }
    }
}