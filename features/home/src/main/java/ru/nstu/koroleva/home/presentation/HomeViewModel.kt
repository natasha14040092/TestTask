package ru.nstu.koroleva.home.presentation

import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.NavController
import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.home.domain.usecase.ClearUserDataUseCase
import ru.nstu.koroleva.home.domain.usecase.GetUserDataUseCase
import ru.nstu.koroleva.home.domain.usecase.SetUserDataUseCase
import ru.nstu.koroleva.home.ui.UserInfoDialogFragment
import ru.nstu.koroleva.n.navigation.LOGIN_URI
import ru.nstu.koroleva.n.resources.ui.DatePickerDialogFragment

class HomeViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val setUserDataUseCase: SetUserDataUseCase,
) : ViewModel() {
    private companion object {
        const val EMPTY_TEXT = ""
        const val REQUEST_KEY = "REQUEST_KEY"
        const val SELECTED_DATE = "SELECTED_DATE"
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

    fun openUserInfoDialog(
        supportFragmentManager: FragmentManager
    ) {
        val userEntity = getUserDataUseCase()
        _state.value = HomeState.Dialog(
            userEntity.name,
            userEntity.surname,
            userEntity.birthdate,
            userEntity.password
        )
        UserInfoDialogFragment(this).show(supportFragmentManager, UserInfoDialogFragment.TAG)
    }

    fun setNameText(nameText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(userName = nameText)
    }

    fun setSurnameText(surnameText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(userSurname = surnameText)
    }

    fun setBirthdateText(birthdateText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(userBirthdate = birthdateText)
    }

    fun openDatePicker(
        supportFragmentManager: FragmentManager,
        viewLifecycleOwner: LifecycleOwner
    ) {
        val datePickerFragment = DatePickerDialogFragment()

        supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY, viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == REQUEST_KEY) {
                val date = bundle.getString(SELECTED_DATE)
                if (date != null) {
                    setBirthdateText(date)
                }
            }
        }

        datePickerFragment.show(supportFragmentManager, DatePickerDialogFragment.TAG)
    }

    fun onClickSaveButton() {
        val currentState = _state.value as? HomeState.Dialog ?: return

        val userEntity =
            UserEntity(
                currentState.userName,
                currentState.userSurname,
                currentState.userBirthdate,
                currentState.password
            )
        setUserDataUseCase(userEntity)
    }

    fun logOut(navController: NavController) {
        clearUserDataUseCase()
        navController.popBackStack()
        navController.navigate(LOGIN_URI.toUri())
    }
}

class HomeViewModelFactory(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val setUserDataUseCase: SetUserDataUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(
                getUserDataUseCase,
                clearUserDataUseCase,
                setUserDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}