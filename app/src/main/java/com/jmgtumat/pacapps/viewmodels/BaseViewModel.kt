package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    protected val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    protected fun setLoading() {
        _loading.value = true
        _error.value = null
    }

    protected fun setSuccess() {
        _loading.value = false
        _error.value = null
    }

    protected fun setError(errorMessage: String?) {
        _loading.value = false
        _error.value = errorMessage
    }
}
