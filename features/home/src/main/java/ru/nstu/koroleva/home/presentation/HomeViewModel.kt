package ru.nstu.koroleva.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nstu.koroleva.home.domain.usecase.GetUserDataUseCase

class HomeViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    private companion object {
        const val EMPTY_TEXT = ""
    }

    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> get() = _state

    init {
        _state.value = HomeState.Content(getUserDataUseCase().name)
    }

    fun loadUserName(): String {
        val currentState = _state.value as? HomeState.Content ?: return EMPTY_TEXT
        return currentState.userName
    }

    fun openUserInfoDialog() {
        _state.value = HomeState.Dialog(getUserDataUseCase())

    }

    //Todo
    fun logOut() {

    }
}

class HomeViewModelFactory(private val getUserDataUseCase: GetUserDataUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(getUserDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}