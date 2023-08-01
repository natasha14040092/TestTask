package ru.nstu.koroleva.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nstu.koroleva.home.presentation.HomeViewModel
import ru.nstu.koroleva.n.home.R
import ru.nstu.koroleva.n.home.databinding.FragmentMainBinding
import ru.nstu.koroleva.n.home.databinding.FragmentUserInfoDialogBinding

class UserInfoDialogFragment(private val viewModel: HomeViewModel) : Fragment() {
    private var _binding: FragmentUserInfoDialogBinding? = null
    private val binding: FragmentUserInfoDialogBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

}