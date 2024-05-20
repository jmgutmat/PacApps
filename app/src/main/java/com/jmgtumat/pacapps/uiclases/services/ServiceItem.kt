package com.jmgtumat.pacapps.uiclases.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Servicio

@Composable
fun ServiceItem(
    servicio: Servicio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = servicio.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = servicio.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$${servicio.precio}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}