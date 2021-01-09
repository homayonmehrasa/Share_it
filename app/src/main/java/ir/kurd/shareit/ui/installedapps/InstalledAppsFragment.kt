package ir.kurd.shareit.ui.installedapps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.kurd.shareit.databinding.FragmentInstalledappsBinding
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class InstalledAppsFragment : BaseFragment<InstalledAppsVM,FragmentInstalledappsBinding >() {

    override val vm : InstalledAppsVM by viewModel()
    override fun onCreateView(inflater: LayoutInflater
                              , container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=FragmentInstalledappsBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {
        getAppList()

    }

    fun getAppList(){

        val pm = requireContext().packageManager
        val installedApplications =  pm.getInstalledApplications(0)
        Toast.makeText(requireContext(), "i",Toast.LENGTH_LONG).show()

    }

}