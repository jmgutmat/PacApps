package com.jmgtumat.pacapps.uiclases.clients

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
import com.jmgtumat.pacapps.data.Cliente

@Composable
fun ClientItem(
    cliente: Cliente,
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
                text = "Nombre: ${cliente.nombre} ${cliente.apellidos}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Teléfono: ${cliente.telefono}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Correo Electrónico: ${cliente.correoElectronico}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Aquí puedes agregar más información del cliente si lo deseas
        }
    }
}
