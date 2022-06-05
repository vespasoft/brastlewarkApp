package com.vespadev.brastlewarkapp.ui

import com.vespadev.brastlewarkapp.domain.Result
import java.text.NumberFormat

fun <T> Result<T>.toUIState() = when (this) {
    is Result.Loading -> UIState.Loading
    is Result.Success -> UIState.Success(data = data)
    is Result.Error -> UIState.Error(message = message)
}

fun Double.twoDigitsNumberFormat(): String {
    val percentageFormatter: NumberFormat = NumberFormat.getInstance()
    percentageFormatter.minimumFractionDigits = 1
    percentageFormatter.maximumFractionDigits = 2
    return percentageFormatter.format(this)
}