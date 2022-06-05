package com.vespadev.brastlewarkapp.domain

import java.lang.Exception

// Error Messages
const val DEFAULT_MESSAGE = "¡Ha ocurrido un error inesperado!"

sealed class ResultException(message: String? = null) : Exception(message ?: DEFAULT_MESSAGE) {
    class GenericException(message: String?): ResultException(message)
    class NetworkException(message: String?): ResultException(message)
    class AuthenticationException(message: String?): ResultException(message)
    class ServiceException(message: String?): ResultException(message)
}