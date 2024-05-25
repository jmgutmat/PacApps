package com.jmgtumat.pacapps.util

import android.util.Patterns

// Utilizar la clase ValidationResult para validar los datos
fun validateProfileInput(nombre: String, apellidos: String, telefono: String, correoElectronico: String, password: String): ValidationResult {
    if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || correoElectronico.isEmpty() || password.isEmpty()) {
        return ValidationResult(false, "Todos los campos son obligatorios")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(correoElectronico).matches()) {
        return ValidationResult(false, "El correo electrónico no es válido")
    }
    if (telefono.length != 10) {
        return ValidationResult(false, "El número de teléfono debe tener 10 dígitos")
    }
    return ValidationResult(true, "")
}


