package com.vespadev.brastlewarkapp.domain

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    class Success<out T>(val data: T) : Result<T>()
    class Error(val message: String) : Result<Nothing>()
}
