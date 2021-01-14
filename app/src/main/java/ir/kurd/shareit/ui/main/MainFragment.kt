package ir.kurd.shareit.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.FragmentMainBinding
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment: BaseFragment<MainVM,FragmentMainBinding>() {
    override val vm : MainVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {

        setUpOnClicks()

    }
    fun setUpOnClicks (){
        binding.imagesbtn.setOnClickListener{

            navController.navigate(R.id.action_mainFragment_to_ImagesFragment)
        }

        binding.installedAppsBtn.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_installedAppsFragment)


        }
        binding.filemanagerbtn.setOnClickListener {

            navController.navigate(R.id.action_mainFragment_to_fileManagerFragment)
        }

        binding.musicBtn.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_musicFragment)

        }

        binding.videoBtn.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_videoFragment)
        }
    }


}