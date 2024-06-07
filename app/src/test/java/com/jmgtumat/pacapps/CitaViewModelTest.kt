package com.jmgtumat.pacapps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyString
import org.mockito.Mockito.argThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class CitaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var citaRepository: CitaRepository
    private lateinit var clienteRepository: ClienteRepository
    private lateinit var empleadoRepository: EmpleadoRepository
    private lateinit var viewModel: CitaViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        citaRepository = mock(CitaRepository::class.java)
        clienteRepository = mock(ClienteRepository::class.java)
        empleadoRepository = mock(EmpleadoRepository::class.java)
        viewModel = CitaViewModel(citaRepository, clienteRepository, empleadoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchCitas() = runTest {
        val citas = listOf(
            Cita(id = "1", clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L),
            Cita(id = "2", clienteId = "cliente2", empleadoId = "empleado2", fecha = 1686180800000L)
        )
        `when`(citaRepository.getCitas()).thenReturn(citas)

        viewModel.fetchCitas()

        assertEquals(citas, viewModel.citas.value)
    }

    @Test
    fun testFetchCitasByDate() = runTest {
        val dateInMillis = 1686094400000L // 7 de junio de 2024
        val citas = listOf(
            Cita(id = "1", clienteId = "cliente1", empleadoId = "empleado1", fecha = dateInMillis)
        )
        `when`(citaRepository.getCitasByDate(dateInMillis)).thenReturn(citas)

        viewModel.fetchCitasByDate(dateInMillis)

        assertEquals(citas, viewModel.citas.value)
    }

    @Test
    fun testFetchCitasByEmpleadoId() = runTest {
        val empleadoId = "empleado1"
        val citas = listOf(
            Cita(id = "1", clienteId = "cliente1", empleadoId = empleadoId, fecha = 1686094400000L)
        )
        `when`(empleadoRepository.getCitasAsignadas(empleadoId)).thenReturn(citas)

        viewModel.fetchCitasByEmpleadoId(empleadoId)

        assertEquals(citas, viewModel.citas.value)
    }

    @Test
    fun testFetchCitasByEmpleadoIdAndDate() = runTest {
        val empleadoId = "empleado1"
        val dateInMillis = 1686094400000L // 7 de junio de 2024
        val citas = listOf(
            Cita(id = "1", clienteId = "cliente1", empleadoId = empleadoId, fecha = dateInMillis)
        )
        `when`(citaRepository.getCitasByEmpleadoIdAndDate(empleadoId, dateInMillis)).thenReturn(citas)

        viewModel.fetchCitasByEmpleadoIdAndDate(empleadoId, dateInMillis)

        assertEquals(citas, viewModel.citas.value)
    }

    @Test
    fun testInsertCita() = runTest {
        val cita = Cita(clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L)
        `when`(citaRepository.addCita(cita)).thenReturn("1")

        // Simulamos el historial de citas del cliente
        `when`(clienteRepository.getHistorialCitas(cita.clienteId)).thenReturn(emptyList())
        `when`(clienteRepository.updateHistorialCitas(anyString(), anyList())).thenReturn(Unit)

        // Simulamos las citas asignadas al empleado
        `when`(empleadoRepository.getCitasAsignadas(cita.empleadoId)).thenReturn(emptyList())
        `when`(empleadoRepository.updateCitasAsignadas(anyString(), anyList())).thenReturn(Unit)

        viewModel.insertCita(cita, cita.clienteId)

        verify(citaRepository).addCita(cita)
        verify(clienteRepository).updateHistorialCitas(cita.clienteId, listOf(cita))
        verify(empleadoRepository).updateCitasAsignadas(cita.empleadoId, listOf(cita.id))
    }

    @Test
    fun testUpdateCita() = runTest {
        val cita = Cita(id = "1", clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L)
        `when`(citaRepository.updateCita(cita)).thenReturn(Unit)

        viewModel.updateCita(cita)

        verify(citaRepository).updateCita(cita)
    }

    @Test
    fun testGetCitaById() = runTest {
        val citaId = "1"
        val cita = Cita(id = citaId, clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L)
        `when`(citaRepository.getCitaById(citaId)).thenReturn(cita)

        val result = viewModel.getCitaById(citaId)

        assertEquals(cita, result)
    }

    @Test
    fun testGetCitasByDateRange() {
        val startDate = 1686094400000L // 7 de junio de 2024
        val endDate = 1686180800000L   // 8 de junio de 2024
        val citas = listOf(
            Cita(id = "1", clienteId = "cliente1", empleadoId = "empleado1", fecha = startDate),
            Cita(id = "2", clienteId = "cliente2", empleadoId = "empleado2", fecha = endDate)
        )
        viewModel._citas.value = citas

        val result = viewModel.getCitasByDateRange(startDate, endDate)

        assertEquals(citas, result)
    }

    @Test
    fun testConfirmarCita() = runTest {
        val citaId = "1"
        val cita = Cita(id = citaId, clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L)
        `when`(citaRepository.getCitaById(citaId)).thenReturn(cita)

        viewModel.confirmarCita(citaId)

        verify(citaRepository).updateCita(argThat { it.id == citaId && it.estado == CitaEstado.CONFIRMADA })
    }

    @Test
    fun testCancelarCita() = runTest {
        val citaId = "1"
        val cita = Cita(id = citaId, clienteId = "cliente1", empleadoId = "empleado1", fecha = 1686094400000L)
        `when`(citaRepository.getCitaById(citaId)).thenReturn(cita)

        viewModel.cancelarCita(citaId)

        verify(citaRepository).updateCita(argThat { it.id == citaId && it.estado == CitaEstado.CANCELADA })
    }
}
