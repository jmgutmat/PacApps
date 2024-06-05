package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel base que proporciona funcionalidades comunes para manejar el estado de carga y errores.
 */
open class BaseViewModel : ViewModel() {

    // LiveData para indicar el estado de carga
    protected val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // LiveData para manejar mensajes de error
    protected val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /**
     * Método para establecer el estado de carga.
     */
    protected fun setLoading() {
        _loading.value = true
        _error.value = null
    }

    /**
     * Método para establecer el estado de éxito y limpiar los errores.
     */
    protected fun setSuccess() {
        _loading.value = false
        _error.value = null
    }

    /**
     * Método para establecer un mensaje de error y detener la carga.
     * @param errorMessage Mensaje de error a mostrar.
     */
    protected fun setError(errorMessage: String?) {
        _loading.value = false
        _error.value = errorMessage
    }
}
