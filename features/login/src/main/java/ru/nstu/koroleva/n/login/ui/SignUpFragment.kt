package ru.nstu.koroleva.n.login.ui

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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


class SignUpFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
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

            tvBirthdatePickerSignUp.doOnTextChanged { text, _, _, _ ->
                viewModel.setBirthdateText(text.toString())
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

            tvBirthdatePickerSignUp.setOnClickListener {
                showDatePicker()
                viewModel.changeSignUpButtonState()
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

    private fun showDatePicker() {
        val datePickerFragment = DatePickerDialogFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY", viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                binding.tvBirthdatePickerSignUp.text = date
            }
        }

        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
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
                changeClickSignUpButton(state.signUpButtonClick)
                showNameError(state.nameError, binding.etNameSignUp)
                showNameError(state.surnameError, binding.etSurnameSignUp)
                showPasswordError(state.passwordError)
                showRepeatPasswordError(state.repeatPasswordError)
            }

            is SignUpState.Ok -> viewModel.onStateOk(findNavController())
        }
    }

    private fun changeClickSignUpButton(state: Boolean) {
        when (state) {
            true -> {
                with(binding) {
                    bSignUp.isClickable = true
                    bSignUp.backgroundTintList =
                        context?.getColorStateList(ru.nstu.koroleva.n.resources.R.color.orange_button_500)
                }
            }
            false -> {
                with(binding) {
                    bSignUp.isClickable = false
                    bSignUp.backgroundTintList =
                        context?.getColorStateList(ru.nstu.koroleva.n.resources.R.color.gray_button_500)
                }
            }
        }
    }

    private fun showNameError(error: SignUpErrorState, field: EditText) {
        when (error) {
            is SignUpErrorState.ShortTextError -> field.error =
                getString(R.string.error_message_name_short)

            is SignUpErrorState.SpecialSymbolError -> field.error =
                getString(R.string.error_message_name_contains_special_symbol)

            is SignUpErrorState.NoError -> field.error = null
            else -> return
        }
    }

    private fun showPasswordError(error: SignUpErrorState) {
        when (error) {
            is SignUpErrorState.ShortTextError -> binding.etPasswordSignUp.error =
                getString(R.string.error_message_password_short)

            is SignUpErrorState.SpecialSymbolError -> binding.etPasswordSignUp.error =
                getString(R.string.error_message_without_special_symbol)

            is SignUpErrorState.DigitError -> binding.etPasswordSignUp.error =
                getString(R.string.error_message_without_digit)

            is SignUpErrorState.UppercaseError -> binding.etPasswordSignUp.error =
                getString(R.string.error_message_without_uppercase)

            is SignUpErrorState.NoError -> binding.etPasswordSignUp.error = null
            else -> return
        }
    }

    private fun showRepeatPasswordError(error: SignUpErrorState) {
        when (error) {
            is SignUpErrorState.PasswordMatchError -> binding.etRepeatPasswordSignUp.error =
                getString(R.string.error_message_passwords_dont_match)
            is SignUpErrorState.NoError -> binding.etRepeatPasswordSignUp.error = null
            else -> return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}