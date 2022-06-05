package com.vespadev.brastlewarkapp.ui

sealed class UIState<out T> {

    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    class Success<out T>(val data: T) : UIState<T>()
    class Error(val message: String) : UIState<Nothing>()
}
