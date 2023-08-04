package ru.nstu.koroleva.home.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import ru.nstu.koroleva.home.presentation.DataErrorState
import ru.nstu.koroleva.home.presentation.HomeState
import ru.nstu.koroleva.home.presentation.HomeViewModel
import ru.nstu.koroleva.n.home.R
import ru.nstu.koroleva.n.home.databinding.FragmentUserInfoDialogBinding

class UserInfoDialogFragment(private val viewModel: HomeViewModel) : DialogFragment() {
    companion object {
        const val TAG = "UserInfoDialogFragment"
    }

    private var _binding: FragmentUserInfoDialogBinding? = null
    private val binding: FragmentUserInfoDialogBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showsDialogContent()
        setsOnClickListeners()
        setsObserves()
    }

    private fun setsOnClickListeners() {
        with(binding) {
            etName.doOnTextChanged { text, _, _, _ ->
                viewModel.setNameText(text.toString())
            }

            etSurname.doOnTextChanged { text, _, _, _ ->
                viewModel.setSurnameText(text.toString())
            }

            etBirthdate.setOnClickListener {
                viewModel.openDatePicker(
                    requireActivity().supportFragmentManager,
                    viewLifecycleOwner
                )
            }

            bSaveUserInfo.setOnClickListener {
                viewModel.onClickSaveButton()
                if (viewModel.checkErrors(viewModel.state.value as HomeState.Dialog)) {
                    Toast.makeText(
                        context,
                        getString(R.string.save_user_data_text),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dialog?.dismiss()
                }
            }

            bCloseDialog.setOnClickListener {
                dialog?.dismiss()
            }
        }

    }

    private fun setsObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is HomeState.Dialog) {
                showBirthdateText(it)
                showErrors(it)
            }
        }
    }

    private fun showErrors(homeState: HomeState.Dialog) {
        showNameError(homeState.nameError, binding.wNameField)
        showNameError(homeState.surnameError, binding.wSurnameField)
        showDateError(homeState.birthdateError)
    }

    private fun showNameError(error: DataErrorState, field: TextInputLayout) {
        when (error) {
            is DataErrorState.ShortTextError -> field.error =
                getString(R.string.error_message_name_short)

            is DataErrorState.SpecialSymbolError -> field.error =
                getString(R.string.error_message_name_contains_special_symbol)

            is DataErrorState.NoError -> field.error = null
            else -> return
        }
    }

    private fun showDateError(error: DataErrorState) {
        when (error) {
            is DataErrorState.IntervalError -> binding.wBirthdateField.error =
                getString(R.string.error_message_date_interval_error)

            is DataErrorState.NoError -> binding.wBirthdateField.error = null
            else -> return
        }
    }

    private fun showBirthdateText(state: HomeState.Dialog) {
        binding.etBirthdate.setText(state.birthdateText)
    }

    private fun showsDialogContent() {
        val currentState = viewModel.state.value as? HomeState.Dialog ?: return
        with(binding) {
            etName.setText(currentState.nameText)
            etSurname.setText(currentState.surnameText)
            etBirthdate.setText(currentState.birthdateText)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.changeStateToContent()
        _binding = null
    }

}