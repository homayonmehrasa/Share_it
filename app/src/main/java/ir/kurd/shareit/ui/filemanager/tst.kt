package ir.kurd.shareit.ui.filemanager

import android.widget.Toast
import ir.kurd.shareit.model.file.ListItem
import ir.kurd.shareit.utiles.extensions.getDirectChildrenCount
import ir.kurd.shareit.utiles.extensions.getInternalStoragePath
import ir.kurd.shareit.utiles.extensions.getProperSize
import java.io.File
import java.util.HashMap

//private fun onPermissionResult(isGranted: Boolean) {
//    if(isGranted){
//        showFileManager()
//    }
//}
//
//private fun showFileManager() {
//    val path = requireContext().getInternalStoragePath()
//    getRegularItemsOf(path){path,items->
//        Toast.makeText(requireContext(), "Item received", Toast.LENGTH_SHORT).show()
//    }
//
//}
//
//private fun getRegularItemsOf(path: String, callback: (originalPath: String, items: ArrayList<ListItem>) -> Unit) {
//    val items = ArrayList<ListItem>()
//    val files = File(path).listFiles()?.filterNotNull()
//    if (context == null) {
//        callback(path, items)
//        return
//    }
//
//
//    if (files != null) {
//        for (file in files) {
//            val fileDirItem = getFileDirItemFromFile(file, false, null)
//            if (fileDirItem != null) {
//                items.add(fileDirItem)
//            }
//        }
//    }
//
//    callback(path, items)
//}
//
//private fun getFileDirItemFromFile(file: File, isSortingBySize: Boolean, lastModifieds: HashMap<String, Long>?): ListItem? {
//    val curPath = file.absolutePath
//    val curName = file.name
////        if (!showHidden && curName.startsWith(".")) {
////            return null
////        }
//
//    var lastModified = lastModifieds?.remove(curPath)
//    val isDirectory = if (lastModified != null) false else file.isDirectory
//    val children = if (isDirectory) file.getDirectChildrenCount(false) else 0
//    val size = if (isDirectory) {
//        if (isSortingBySize) {
//            file.getProperSize(false)
//        } else {
//            0L
//        }
//    } else {
//        file.length()
//    }
//
//    if (lastModified == null) {
//        lastModified = file.lastModified()
//    }
//
//    return ListItem(curPath, curName, isDirectory, children, size, lastModified, false)
//}