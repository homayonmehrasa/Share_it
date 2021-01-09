package ir.kurd.shareit.ui.main

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import ir.kurd.shareit.databinding.FragmentMainBinding
import ir.kurd.shareit.model.file.ListItem
import ir.kurd.shareit.ui.base.BaseFragment
import ir.kurd.shareit.utiles.Constants
import ir.kurd.shareit.utiles.extensions.getDirectChildrenCount
import ir.kurd.shareit.utiles.extensions.getInternalStoragePath
import ir.kurd.shareit.utiles.extensions.getProperSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.HashMap

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestStoragePermission{onPermissionResult(it)}
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if(isGranted){
            showFileManager()
        }
    }

    private fun showFileManager() {
        val path = requireContext().getInternalStoragePath()
        getRegularItemsOf(path){path,items->
            Toast.makeText(requireContext(), "Item received", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getRegularItemsOf(path: String, callback: (originalPath: String, items: ArrayList<ListItem>) -> Unit) {
        val items = ArrayList<ListItem>()
        val files = File(path).listFiles()?.filterNotNull()
        if (context == null) {
            callback(path, items)
            return
        }


        if (files != null) {
            for (file in files) {
                val fileDirItem = getFileDirItemFromFile(file, false, null)
                if (fileDirItem != null) {
                    items.add(fileDirItem)
                }
            }
        }

        callback(path, items)
    }

    private fun getFileDirItemFromFile(file: File, isSortingBySize: Boolean, lastModifieds: HashMap<String, Long>?): ListItem? {
        val curPath = file.absolutePath
        val curName = file.name
//        if (!showHidden && curName.startsWith(".")) {
//            return null
//        }

        var lastModified = lastModifieds?.remove(curPath)
        val isDirectory = if (lastModified != null) false else file.isDirectory
        val children = if (isDirectory) file.getDirectChildrenCount(false) else 0
        val size = if (isDirectory) {
            if (isSortingBySize) {
                file.getProperSize(false)
            } else {
                0L
            }
        } else {
            file.length()
        }

        if (lastModified == null) {
            lastModified = file.lastModified()
        }

        return ListItem(curPath, curName, isDirectory, children, size, lastModified, false)
    }


}