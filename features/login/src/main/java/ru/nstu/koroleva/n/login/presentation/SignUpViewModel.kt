package ru.nstu.koroleva.n.login.presentation

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import ru.nstu.koroleva.n.login.domain.entity.UserEntity
import ru.nstu.koroleva.n.login.domain.usecase.SetUserDataUseCase
import ru.nstu.koroleva.n.navigation.HOME_URI

class SignUpViewModel(
    private val setUserDataUseCase: SetUserDataUseCase
) : ViewModel() {

    private companion object {
        const val EMPTY_TEXT = ""
    }

    private val _state = MutableLiveData<SignUpState>()
    val state: LiveData<SignUpState> get() = _state

    init {
        _state.value = SignUpState.Content(
            nameText = EMPTY_TEXT,
            surnameText = EMPTY_TEXT,
            birthdateText = EMPTY_TEXT,
            passwordText = EMPTY_TEXT,
            repeatPasswordText = EMPTY_TEXT,
            signUpButtonClick = false,
            nameError = SignUpErrorState.NoError,
            surnameError = SignUpErrorState.NoError,
            passwordError = SignUpErrorState.NoError,
            repeatPasswordError = SignUpErrorState.NoError,
        )
    }

    fun setNameText(text: String) {
        val currentState = _state.value as? SignUpState.Content ?: return
        _state.value = currentState.copy(nameText = text)
    }

    fun setSurnameText(text: String) {
        val currentState = _state.value as? SignUpState.Content ?: return
        _state.value = currentState.copy(surnameText = text)
    }

    fun setBirthdateText(text: String) {
        val currentState = _state.value as? SignUpState.Content ?: return
        _state.value = currentState.copy(birthdateText = text)
    }

    fun setPasswordText(text: String) {
        val currentState = _state.value as? SignUpState.Content ?: return
        _state.value = currentState.copy(passwordText = text)
    }

    fun setRepeatPasswordText(text: String) {
        val currentState = _state.value as? SignUpState.Content ?: return
        _state.value = currentState.copy(repeatPasswordText = text)
    }

    fun changeSignUpButtonState() {
        val currentState = _state.value as? SignUpState.Content ?: return

        if (currentState.nameText != EMPTY_TEXT && currentState.surnameText != EMPTY_TEXT && currentState.birthdateText != EMPTY_TEXT && currentState.passwordText != EMPTY_TEXT && currentState.repeatPasswordText != EMPTY_TEXT) _state.value =
            currentState.copy(signUpButtonClick = true)
        else _state.value = currentState.copy(signUpButtonClick = false)
    }

    fun onClickSignUpButton() {
        validateNameFields()
        validatePassword()
        validateRepeatPassword()
        checkErrors()
    }

    private fun validateNameFields() {
        val currentState = _state.value as? SignUpState.Content ?: return

        val nameError = validateName(currentState.nameText)
        val surnameError = validateName(currentState.surnameText)

        _state.value = currentState.copy(nameError = nameError, surnameError = surnameError)
    }

    private fun validateName(nameText: String): SignUpErrorState = when {
        nameText.length < 2 -> SignUpErrorState.ShortTextError
        nameText.contains("[!\"#$%&'@()*+,-./:;<=>?\\[\\]^_`{|}~0-9\\s]".toRegex()) -> SignUpErrorState.SpecialSymbolError
        else -> SignUpErrorState.NoError
    }

    private fun validatePassword() {
        val currentState = _state.value as? SignUpState.Content ?: return
        val passwordText = currentState.passwordText

        when {
            passwordText.length < 8 -> _state.value =
                currentState.copy(passwordError = SignUpErrorState.ShortTextError)

            !passwordText.contains("[!@#$%^&*()—_+=;:,./?|`~{}\\[\\]]".toRegex()) -> _state.value =
                currentState.copy(passwordError = SignUpErrorState.SpecialSymbolError)

            !passwordText.contains("[0-9]".toRegex()) -> _state.value =
                currentState.copy(passwordError = SignUpErrorState.DigitError)

            !passwordText.contains("[A-ZА-Я]".toRegex()) -> _state.value =
                currentState.copy(passwordError = SignUpErrorState.UppercaseError)

            else -> _state.value = currentState.copy(passwordError = SignUpErrorState.NoError)
        }
    }

    private fun validateRepeatPassword() {
        val currentState = _state.value as? SignUpState.Content ?: return

        when {
            currentState.passwordText != currentState.repeatPasswordText -> _state.value =
                currentState.copy(repeatPasswordError = SignUpErrorState.PasswordMatchError)
            else -> _state.value = currentState.copy(repeatPasswordError = SignUpErrorState.NoError)
        }
    }

    private fun checkErrors() {
        val currentState = _state.value as? SignUpState.Content ?: return

        if (currentState.nameError is SignUpErrorState.NoError &&
            currentState.surnameError is SignUpErrorState.NoError &&
            currentState.passwordError is SignUpErrorState.NoError &&
            currentState.repeatPasswordError is SignUpErrorState.NoError
        ) {
            saveUserInfo(currentState)
            _state.value = SignUpState.Ok
        }
    }

    private fun saveUserInfo(currentState: SignUpState.Content) {
        val userEntity = UserEntity(
            currentState.nameText,
            currentState.surnameText,
            currentState.birthdateText,
            currentState.passwordText
        )
        setUserDataUseCase(userEntity)
    }

    fun goToHomeScreen(navController: NavController) {
        val homeUri = HOME_URI.toUri()
        navController.navigate(homeUri)
    }
}

class SignUpViewModelFactory(private val setUserDataUseCase: SetUserDataUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SignUpViewModel(setUserDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}