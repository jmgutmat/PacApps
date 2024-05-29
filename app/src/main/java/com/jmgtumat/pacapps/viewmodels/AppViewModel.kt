//package com.jmgtumat.pacapps.viewmodels
//
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//class AppViewModel(
//    val citaViewModel: CitaViewModel,
//    val clienteViewModel: ClienteViewModel,
//    val empleadoViewModel: EmpleadoViewModel,
//    val servicioViewModel: ServicioViewModel
//) : ViewModel() {
//}
//
//class AppViewModelFactory(
//    private val citaViewModel: CitaViewModel,
//    private val clienteViewModel: ClienteViewModel,
//    private val empleadoViewModel: EmpleadoViewModel,
//    private val servicioViewModel: ServicioViewModel
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
//        if (viewModelClass.isAssignableFrom(AppViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return AppViewModel(citaViewModel, clienteViewModel, empleadoViewModel, servicioViewModel) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//@Composable
//fun ProvideAppViewModel(content: @Composable (AppViewModel) -> Unit) {
//    val appViewModel = viewModel<AppViewModel>()
//    content(appViewModel)
//}