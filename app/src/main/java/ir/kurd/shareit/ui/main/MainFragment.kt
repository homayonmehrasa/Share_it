package ir.kurd.shareit.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.kurd.shareit.databinding.FragmentMainBinding
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment: BaseFragment<MainVM,FragmentMainBinding>() {
    override val vm: MainVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }
}