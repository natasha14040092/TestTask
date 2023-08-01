package ru.nstu.koroleva.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.nstu.koroleva.home.data.datasource.UserDataSourceImpl
import ru.nstu.koroleva.home.data.repository.UserDataRepositoryImpl
import ru.nstu.koroleva.home.domain.repository.UserDataRepository
import ru.nstu.koroleva.home.domain.usecase.ClearUserDataUseCase
import ru.nstu.koroleva.home.domain.usecase.GetUserDataUseCase
import ru.nstu.koroleva.home.presentation.HomeViewModel
import ru.nstu.koroleva.home.presentation.HomeViewModelFactory
import ru.nstu.koroleva.n.home.R
import ru.nstu.koroleva.n.home.databinding.FragmentHomeBinding
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        showUserName()
        setsOnClickListeners()
        setsObservers()
    }

    private fun initViewModel() {
        val userDataRepository = initRepository()
        val viewModelFactory = HomeViewModelFactory(
            GetUserDataUseCase(userDataRepository),
            ClearUserDataUseCase(userDataRepository)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    private fun initRepository(): UserDataRepository {
        val userSharedPreferencesProvider =
            UserSharedPreferencesProvider.getInstance(requireContext())
        val userDataSource = UserDataSourceImpl(userSharedPreferencesProvider)
        return UserDataRepositoryImpl(userDataSource)
    }

    private fun showUserName() {
        binding.tvGreeting.text =
            getString(R.string.welcome_text) + ", " + viewModel.loadUserName() + "!"
    }


    private fun setsOnClickListeners() {
        with(binding) {
            bGreeting.setOnClickListener {
                viewModel.openUserInfoDialog()
            }

            ibLogOut.setOnClickListener {
                viewModel.logOut(findNavController())
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