package ru.nstu.koroleva.n.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

    }

    private fun setsObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}