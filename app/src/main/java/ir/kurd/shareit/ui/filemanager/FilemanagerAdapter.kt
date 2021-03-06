package ir.kurd.shareit.ui.filemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kurd.shareit.databinding.ItemFileManagerBinding
import ir.kurd.shareit.databinding.ItemFileManagerFileBinding
import ir.kurd.shareit.model.file.FilesModel

class FilemanagerAdapter ( val fileslist : ArrayList<FilesModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener: ClickListener? = null

    override fun getItemViewType(position: Int): Int {
        val isDir = fileslist[position].mIsDirectory
        return if (isDir) {
            VIEW_FOLDER
        }else VIEW_FILE


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_FOLDER){
            val binding = ItemFileManagerBinding.inflate(LayoutInflater.from(parent.context) , parent ,false)
            return DirViewHolder(binding)

        }else{
            val binding = ItemFileManagerFileBinding.inflate(LayoutInflater.from(parent.context), parent , false)
            return FileViewHolder(binding)

        }

    }

    override fun getItemCount(): Int {
     return   fileslist.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item =fileslist[position]
       when (holder.itemViewType){
           VIEW_FOLDER ->{

               holder as DirViewHolder
               holder.dirName.text = item.mName
               holder.dirDesc.text = item.mChildren.toString()
               holder.itemView.setOnClickListener{
                   clickListener?.onFolderClick(item)
               }

           }
           VIEW_FILE ->{

               holder as FileViewHolder
               val size = if(item.mSize/1024< 1000)"${item.mSize/1024 }KB "
               else "${item.mSize/(1024*1024) }MB"

               holder.nameFile.text= item.mName
               holder.size.text =size

               holder.itemView.setOnClickListener {
                   clickListener?.onFileClick(item)
               }



           }
       }

    }

    interface ClickListener {


        fun onFolderClick(item : FilesModel)
        fun onFileClick(item : FilesModel)
    }

    fun setOnItemClickListener(clickListener: ClickListener) {

        this.clickListener = clickListener

    }


    inner class FileViewHolder
    (binding: ItemFileManagerFileBinding)
        :RecyclerView.ViewHolder(binding.root)  {

        val imgFile = binding.fileImageView
        val nameFile = binding.fileName
        val size = binding.fileDesc


    }

    inner class DirViewHolder(binding:ItemFileManagerBinding) : RecyclerView.ViewHolder(binding.root) {
        val dirImg = binding.itemImageView
        val dirName = binding.itemName
        val dirDesc = binding.itemDesc



    }

    fun updateData(newData:ArrayList<FilesModel>){
        fileslist.clear()
        fileslist.addAll(newData)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_FILE = 1
        const val VIEW_FOLDER= 2

    }

}