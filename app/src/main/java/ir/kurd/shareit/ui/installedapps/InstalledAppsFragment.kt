package ir.kurd.shareit.ui.installedapps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ir.kurd.shareit.databinding.FragmentInstalledappsBinding
import ir.kurd.shareit.model.app.InstalledAppModel
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class InstalledAppsFragment : BaseFragment<InstalledAppsVM,FragmentInstalledappsBinding >() {
    val appListAdapter =InstalledAppAdapter(arrayListOf())
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

    private fun getAppList(){

        val pm = requireContext().packageManager
        val installedApplications =  pm.getInstalledApplications(0)
        val modelList=   installedApplications.map {
            val name = pm.getApplicationLabel(it)
            val sizeInt = File(it.sourceDir).length() / (1024)
            val size = if (sizeInt > 999) {
                "${File(it.sourceDir).length() / (1024*1024)}MB"

            } else {
                "${File(it.sourceDir).length() / (1024)}KB"
            }
            val icon = pm.getApplicationIcon(it)

            InstalledAppModel(name.toString()?:"" , size = size , icon,it.sourceDir?:"")
        }
        binding.installedAppsRecyclerView.adapter=appListAdapter
        appListAdapter.updateData(modelList as ArrayList<InstalledAppModel>)

    }

}