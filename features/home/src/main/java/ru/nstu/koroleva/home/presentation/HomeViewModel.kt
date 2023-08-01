package ru.nstu.koroleva.home.presentation

import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.NavController
import ru.nstu.koroleva.home.domain.usecase.ClearUserDataUseCase
import ru.nstu.koroleva.home.domain.usecase.GetUserDataUseCase
import ru.nstu.koroleva.home.ui.UserInfoDialogFragment
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
        changeStateToContent()
    }

    fun changeStateToContent() {
        _state.value = HomeState.Content(getUserDataUseCase().name)
    }

    fun loadUserName(): String {
        val currentState = _state.value as? HomeState.Content ?: return EMPTY_TEXT
        return currentState.userName
    }

    fun logOut(navController: NavController) {
        clearUserDataUseCase()
        navController.popBackStack()
        navController.navigate(LOGIN_URI.toUri())
    }

    fun openUserInfoDialog(
        supportFragmentManager: FragmentManager
    ) {
            _state.value = HomeState.Dialog(getUserDataUseCase())
            UserInfoDialogFragment(this).show(supportFragmentManager, UserInfoDialogFragment.TAG)
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