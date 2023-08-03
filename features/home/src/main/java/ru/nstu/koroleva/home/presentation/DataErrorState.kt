package ru.nstu.koroleva.home.presentation

sealed class DataErrorState {
    object NoError : DataErrorState()
    object ShortTextError : DataErrorState()
    object SpecialSymbolError : DataErrorState()
    object IntervalError : DataErrorState()
}

