package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.ViewModel
import com.jmgtumat.pacapps.data.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class AppointmentSummaryViewModel : ViewModel() {

    private val _selectedServices = MutableStateFlow<List<Servicio>>(emptyList())
    val selectedServices: StateFlow<List<Servicio>> get() = _selectedServices

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> get() = _selectedDate

    private val _selectedTime = MutableStateFlow<String?>(null)
    val selectedTime: StateFlow<String?> get() = _selectedTime

    fun setSelectedServices(services: List<Servicio>) {
        _selectedServices.value = services
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setSelectedTime(time: String) {
        _selectedTime.value = time
    }

    fun clearSummary() {
        _selectedServices.value = emptyList()
        _selectedDate.value = null
        _selectedTime.value = null
    }
}
