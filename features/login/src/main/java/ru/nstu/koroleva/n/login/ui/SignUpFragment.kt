package ru.nstu.koroleva.n.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.nstu.koroleva.n.login.R
import ru.nstu.koroleva.n.login.presentation.SignUpViewModel
import ru.nstu.koroleva.n.login.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    private fun setsOnClickListeners() {
        with(binding){
            tvBirthdatePickerSignUp.setOnClickListener {
                val datePickerFragment = DatePickerDialogFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // определяю setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tvBirthdatePickerSignUp.text = date
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

        }
    }

    private fun setsObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}