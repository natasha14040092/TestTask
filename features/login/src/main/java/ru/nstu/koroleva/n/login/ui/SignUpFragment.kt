package ru.nstu.koroleva.n.login.ui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import ru.nstu.koroleva.n.login.R
import ru.nstu.koroleva.n.login.data.datasource.UserDataSourceImpl
import ru.nstu.koroleva.n.login.data.repository.UserDataRepositoryImpl
import ru.nstu.koroleva.n.login.databinding.FragmentSignUpBinding
import ru.nstu.koroleva.n.login.domain.usecase.SetUserDataUseCase
import ru.nstu.koroleva.n.login.presentation.SignUpErrorState
import ru.nstu.koroleva.n.login.presentation.SignUpState
import ru.nstu.koroleva.n.login.presentation.SignUpViewModel
import ru.nstu.koroleva.n.login.presentation.SignUpViewModelFactory
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider
import ru.nstu.koroleva.n.resources.ui.setBackgroundAnimation


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        setBackgroundAnimation(binding.root.background as AnimationDrawable)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setsOnClickListeners()
        setsObservers()
    }

    private fun initViewModel() {
        val setUserDataUseCase =  initUseCase()
        val viewModelFactory = SignUpViewModelFactory(setUserDataUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }

    private fun initUseCase() : SetUserDataUseCase  {
        val userSharedPreferencesProvider = UserSharedPreferencesProvider.getInstance(requireContext())
        val userDataSource = UserDataSourceImpl(userSharedPreferencesProvider)
        val userDataRepository = UserDataRepositoryImpl(userDataSource)
        return SetUserDataUseCase(userDataRepository)
    }

    private fun setsOnClickListeners() {
        with(binding) {
            etNameSignUp.doOnTextChanged { text, _, _, _ ->
                viewModel.setNameText(text.toString())
                viewModel.changeSignUpButtonState()
            }

            etSurnameSignUp.doOnTextChanged { text, _, _, _ ->
                viewModel.setSurnameText(text.toString())
                viewModel.changeSignUpButtonState()
            }

            etPasswordSignUp.doOnTextChanged { text, _, _, _ ->
                viewModel.setPasswordText(text.toString())
                viewModel.changeSignUpButtonState()
            }

            etRepeatPasswordSignUp.doOnTextChanged { text, _, _, _ ->
                viewModel.setRepeatPasswordText(text.toString())
                viewModel.changeSignUpButtonState()
            }

            etBirthdatePickerSignUp.setOnClickListener {
                viewModel.openDatePicker(requireActivity().supportFragmentManager, viewLifecycleOwner)
            }

            ibShowPassword.setOnClickListener {
                showPassword(ibShowPassword, etPasswordSignUp)
            }

            ibShowRepeatPassword.setOnClickListener {
                showPassword(ibShowRepeatPassword, etRepeatPasswordSignUp)
            }

            bSignUp.setOnClickListener {
                viewModel.onClickSignUpButton()
            }
        }
    }

    private fun showPassword(button: ImageButton, field: EditText) {
        when (field.transformationMethod) {
            HideReturnsTransformationMethod.getInstance() -> {
                button.setImageResource(R.drawable.ic_eye_close)
                field.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            PasswordTransformationMethod.getInstance() -> {
                button.setImageResource(R.drawable.ic_eye_open)
                field.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    private fun setsObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            onChangeState(it)
        }
    }

    private fun onChangeState(state: SignUpState) {
        when (state) {
            is SignUpState.Content -> {
                showDate(state.birthdateText)
                changeClickSignUpButton(state.signUpButtonClick)
                showNameError(state.nameError,binding.wNameField)
                showNameError(state.surnameError, binding.wSurnameField)
                showDateError(state.birthdateError)
                showPasswordError(state.passwordError)
                showRepeatPasswordError(state.repeatPasswordError)
            }

            is SignUpState.Ok -> {
                Toast.makeText(context, getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show()
                viewModel.goToHomeScreen(findNavController())
            }
        }
    }

    private fun showDate(date: String) {
        binding.etBirthdatePickerSignUp.setText(date)
    }

    private fun changeClickSignUpButton(stateSignUpButton: Boolean) {
        when (stateSignUpButton) {
            true -> {
                with(binding) {
                    bSignUp.isClickable = true
                    bSignUp.backgroundTintList =
                        requireContext().getColorStateList(ru.nstu.koroleva.n.resources.R.color.pink)
                }
            }
            false -> {
                with(binding) {
                    bSignUp.isClickable = false
                    bSignUp.backgroundTintList =
                        requireContext().getColorStateList(ru.nstu.koroleva.n.resources.R.color.gray)
                }
            }
        }
    }

    private fun showNameError(error: SignUpErrorState, field: TextInputLayout) {
        when (error) {
            is SignUpErrorState.ShortTextError -> field.error =
                getString(R.string.error_message_name_short)

            is SignUpErrorState.SpecialSymbolError -> field.error =
                getString(R.string.error_message_name_contains_special_symbol)

            is SignUpErrorState.NoError -> field.error = null
            else -> return
        }
    }

    private fun showDateError(error: SignUpErrorState) {
        when (error) {
            is SignUpErrorState.IntervalError -> binding.wBirthdateField.error =
                getString(R.string.error_message_date_interval_error)

            is SignUpErrorState.NoError -> binding.wBirthdateField.error = null
            else -> return
        }

    }

    private fun showPasswordError(error: SignUpErrorState) {
        when (error) {
            is SignUpErrorState.ShortTextError -> binding.wPasswordField.error =
                getString(R.string.error_message_password_short)

            is SignUpErrorState.SpecialSymbolError -> binding.wPasswordField.error =
                getString(R.string.error_message_without_special_symbol)

            is SignUpErrorState.DigitError -> binding.wPasswordField.error =
                getString(R.string.error_message_without_digit)

            is SignUpErrorState.UppercaseError -> binding.wPasswordField.error =
                getString(R.string.error_message_without_uppercase)

            is SignUpErrorState.NoError -> binding.wPasswordField.error = null
            else -> return
        }
    }

    private fun showRepeatPasswordError(error: SignUpErrorState) {
        when (error) {
            is SignUpErrorState.PasswordMatchError -> binding.wRepeatPasswordField.error =
                getString(R.string.error_message_passwords_dont_match)
            is SignUpErrorState.NoError -> binding.wRepeatPasswordField.error = null
            else -> return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}