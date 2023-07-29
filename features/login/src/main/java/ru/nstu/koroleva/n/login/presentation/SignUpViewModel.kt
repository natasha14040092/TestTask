package ru.nstu.koroleva.n.login.presentation

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ru.nstu.koroleva.n.navigation.HOME_URI

class SignUpViewModel : ViewModel() {
    private val _state = MutableLiveData<SignUpState>()
    val state: LiveData<SignUpState> get() = _state
}