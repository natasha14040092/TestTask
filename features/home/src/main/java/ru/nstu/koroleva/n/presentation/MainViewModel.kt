package ru.nstu.koroleva.n.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> get() = _state


}