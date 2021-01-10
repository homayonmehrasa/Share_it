package ir.kurd.shareit.ui.filemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.kurd.shareit.databinding.FragmentFileManagerBinding
import ir.kurd.shareit.model.file.FilesModel
import ir.kurd.shareit.ui.base.BaseFragment
import ir.kurd.shareit.utiles.extensions.getDirectChildrenCount
import ir.kurd.shareit.utiles.extensions.getInternalStoragePath
import ir.kurd.shareit.utiles.extensions.getProperSize
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.HashMap

class FileManagerFragment : BaseFragment <FileManagerVM , FragmentFileManagerBinding> (){

    val filemanagerAdapter = FilemanagerAdapter(arrayListOf())

    override val vm : FileManagerVM by viewModel()
    override fun onCreateView(inflater: LayoutInflater
                              , container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=FragmentFileManagerBinding.inflate(inflater)
        return binding.root

}

    override fun init() {
       requestStoragePermission {
           onPermissionResult(it)
       }
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if(isGranted){
            showFileManager()
        }
    }
    private fun showFileManager() {
        val path = requireContext().getInternalStoragePath()
        getRegularItemsOf(path){path,items->


            Toast.makeText(requireContext(), "Item received ${items.size}", Toast.LENGTH_SHORT).show()


            val sortedItems = items.sortedWith(compareByDescending(FilesModel::mIsDirectory).thenBy(FilesModel::mName))
            binding.FilemanagerRecyclerView.adapter = filemanagerAdapter
            filemanagerAdapter.updateData(ArrayList(sortedItems))

        }

    }

    private fun getRegularItemsOf(path: String, callback: (originalPath: String, items: ArrayList<FilesModel>) -> Unit) {
        val items = ArrayList<FilesModel>()
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

    private fun getFileDirItemFromFile(file: File, isSortingBySize: Boolean, lastModifieds: HashMap<String, Long>?): FilesModel? {
        val curPath = file.absolutePath
        val curName = file.name
       // if (!showHidden && curName.startsWith(".")) {
        //    return null
   //     }

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

        return FilesModel(curPath, curName, isDirectory, children, size, lastModified, false)
    }

}