package ru.nstu.koroleva.home.presentation

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import ru.nstu.koroleva.home.domain.usecase.ClearUserDataUseCase
import ru.nstu.koroleva.home.domain.usecase.GetUserDataUseCase
import ru.nstu.koroleva.n.navigation.LOGIN_URI

class HomeViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase
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

    fun logOut(navController: NavController) {
        clearUserDataUseCase()
        navController.popBackStack()
        navController.navigate(LOGIN_URI.toUri())
    }
}

class HomeViewModelFactory(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(
                getUserDataUseCase,
                clearUserDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}