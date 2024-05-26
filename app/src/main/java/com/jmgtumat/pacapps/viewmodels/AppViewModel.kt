package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.ViewModel

class AppViewModel(
    val citaViewModel: CitaViewModel,
    val clienteViewModel: ClienteViewModel,
    val empleadoViewModel: EmpleadoViewModel,
    val servicioViewModel: ServicioViewModel
) : ViewModel() {
}
