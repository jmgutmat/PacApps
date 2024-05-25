package com.jmgtumat.pacapps.util

// Clase para representar el resultado de la validación
data class ValidationResult(val isValid: Boolean, val errorMessage: String)

// Función para validar el correo electrónico
fun validateEmail(email: String): ValidationResult {
    val emailRegex = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")
    return if (email.isBlank()) {
        ValidationResult(false, "El correo electrónico no puede estar en blanco")
    } else if (!emailRegex.matches(email)) {
        ValidationResult(false, "Formato de correo electrónico inválido")
    } else {
        ValidationResult(true, "")
    }
}

// Función para validar la contraseña
fun validatePassword(password: String): ValidationResult {
    val passwordRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""")
    return if (password.isBlank()) {
        ValidationResult(false, "La contraseña no puede estar en blanco")
    } else if (!passwordRegex.matches(password)) {
        ValidationResult(false, "La contraseña debe tener al menos 8 caracteres y contener una combinación de mayúsculas, minúsculas, números y caracteres especiales")
    } else {
        ValidationResult(true, "")
    }
}

// Función para validar la entrada de datos
fun validateInputFields(email: String, password: String): ValidationResult {
    val emailValidationResult = validateEmail(email)
    val passwordValidationResult = validatePassword(password)

    return if (!emailValidationResult.isValid) {
        emailValidationResult
    } else if (!passwordValidationResult.isValid) {
        passwordValidationResult
    } else {
        ValidationResult(true, "")
    }
}
