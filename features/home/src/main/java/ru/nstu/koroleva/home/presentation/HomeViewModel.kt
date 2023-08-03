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
import java.text.SimpleDateFormat
import java.util.*

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
            nameText = userEntity.name,
            surnameText = userEntity.surname,
            birthdateText = userEntity.birthdate,
            passwordText = userEntity.password,
            nameError = DataErrorState.NoError,
            surnameError = DataErrorState.NoError,
            birthdateError = DataErrorState.NoError
        )
        UserInfoDialogFragment(this).show(supportFragmentManager, UserInfoDialogFragment.TAG)
    }

    fun setNameText(nameText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(nameText = nameText)
    }

    fun setSurnameText(surnameText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(surnameText = surnameText)
    }

    fun setBirthdateText(birthdateText: String) {
        val currentState = _state.value as? HomeState.Dialog ?: return
        _state.value = currentState.copy(birthdateText = birthdateText)
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
        validateUserData()
        saveData()
    }

    private fun validateUserData() {
        validateNameFields()
        validateBirthdate()
    }

    private fun validateNameFields() {
        val currentState = _state.value as? HomeState.Dialog ?: return

        val nameError = validateName(currentState.nameText)
        val surnameError = validateName(currentState.surnameText)

        _state.value = currentState.copy(nameError = nameError, surnameError = surnameError)
    }

    private fun validateName(nameText: String): DataErrorState = when {
        nameText.length < 2 -> DataErrorState.ShortTextError
        nameText.contains("[!\"#$%&'@()*+,-./:;<=>?\\[\\]^_`{|}~0-9\\s]".toRegex()) -> DataErrorState.SpecialSymbolError
        else -> DataErrorState.NoError
    }

    private fun validateBirthdate() {
        val currentState = _state.value as? HomeState.Dialog ?: return

        val selectedDate =
            SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(currentState.birthdateText)
                ?: return

        val cmp = selectedDate.compareTo(Date())
        when {
            cmp > 0 ->
                _state.value = currentState.copy(birthdateError = DataErrorState.IntervalError)

            cmp < 0 ->
                _state.value = currentState.copy(birthdateError = DataErrorState.NoError)
        }
    }

    private fun saveData() {
        val currentState = _state.value as? HomeState.Dialog ?: return

        if (checkErrors(currentState)) {
            val userEntity =
                UserEntity(
                    currentState.nameText,
                    currentState.surnameText,
                    currentState.birthdateText,
                    currentState.passwordText
                )
            setUserDataUseCase(userEntity)
        }
    }

    fun checkErrors(currentState: HomeState.Dialog): Boolean {
        return currentState.nameError is DataErrorState.NoError &&
                currentState.surnameError is DataErrorState.NoError &&
                currentState.birthdateError is DataErrorState.NoError
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