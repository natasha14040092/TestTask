package ru.nstu.koroleva.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
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

            tvBirthdate.setOnClickListener {
                viewModel.openDatePicker(
                    requireActivity().supportFragmentManager,
                    viewLifecycleOwner
                )
            }

            bSaveUserInfo.setOnClickListener {
                viewModel.onClickSaveButton()
                Toast.makeText(context, getString(R.string.save_user_data_text), Toast.LENGTH_SHORT)
                    .show()
                dialog?.dismiss()
            }

            bCloseDialog.setOnClickListener {
                dialog?.dismiss()
            }
        }

    }

    private fun setsObserves() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is HomeState.Dialog) showBirthdateText(it)
        }

    }

    private fun showBirthdateText(state: HomeState.Dialog) {
        binding.tvBirthdate.text = state.userBirthdate
    }

    private fun showsDialogContent() {
        val currentState = viewModel.state.value as? HomeState.Dialog ?: return
        with(binding) {
            etName.setText(currentState.userName)
            etSurname.setText(currentState.userSurname)
            tvBirthdate.text = currentState.userBirthdate
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.changeStateToContent()
        _binding = null
    }

}